package fr.insalyon.dasi.collectif.util;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class PasswordStorage {
    private static final int iterations = 10000;
    private static final int keyLength = 256;
    private static final int saltLength = 32;
    private static final String storageSeparator = ":";

    public static String newStorageString(final String password) {
        SecureRandom random = new SecureRandom();
        byte salt[] = new byte[saltLength];
        random.nextBytes(salt);

        String hashedPassword = bytesToHex(hashPassword(password.toCharArray(), salt, iterations, keyLength));

        return  hashedPassword +
                storageSeparator +
                bytesToHex(salt) +
                storageSeparator +
                String.valueOf(iterations);
    }

    public static boolean checkPassword(final String password, final String storageString) {
        String[] storageElements = storageString.split(storageSeparator);
        byte[] hashedPassword = hexToBytes(storageElements[0]);

        byte[] hashedPasswordVerif = hashPassword(
                password.toCharArray(),
                hexToBytes(storageElements[1]),
                Integer.valueOf(storageElements[2]),
                keyLength);

        return Arrays.equals(hashedPasswordVerif, hashedPassword);
    }

    private static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    private static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    private static byte[] hashPassword( final char[] password, final byte[] salt, final int iterations, final int keyLength ) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            PBEKeySpec spec = new PBEKeySpec( password, salt, iterations, keyLength );
            SecretKey key = skf.generateSecret( spec );
            byte[] res = key.getEncoded( );
            return res;

        } catch( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }
}
