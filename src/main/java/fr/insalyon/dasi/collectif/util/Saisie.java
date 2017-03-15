package fr.insalyon.dasi.collectif.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author DASI Team
 */
public class Saisie {

    public static Date readDate(String invite, String dateFormat) {
        DateFormat format = new SimpleDateFormat(dateFormat);
        Date date = null;

        while (date == null) {
            String line = readString(invite);
            try {
                date = format.parse(line);
            } catch (ParseException e) {
                System.out.println("/!\\ Erreur de saisie - Date au format " + dateFormat + " attendue /!\\");
            }
        }
        return date;
    }


    public static String readString(String invite) {
        return readString(invite, false);
    }


    public static String readString(String invite, boolean allowEmpty) {
        String chaineLue = null;
        System.out.print(invite);
        while (chaineLue == null) {
            try {
                InputStreamReader isr = new InputStreamReader(System.in);
                BufferedReader br = new BufferedReader(isr);
                chaineLue = br.readLine();
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
            if (chaineLue != null && !allowEmpty && chaineLue.isEmpty()) {
                System.out.println("/!\\ Erreur de saisie - La chaine ne doit pas être vide /!\\");
                chaineLue = null;
            }
        }
        return chaineLue;
    }


    public static Integer readInteger(String invite) {
        Integer valeurLue = null;
        while (valeurLue == null) {
            try {
                valeurLue = Integer.parseInt(readString(invite));
            } catch (NumberFormatException ex) {
                System.out.println("/!\\ Erreur de saisie - Nombre entier attendu /!\\");
            }
        }
        return valeurLue;
    }

    public static Integer readInteger(String invite, List<Integer> valeursPossibles) {
        Integer valeurLue = null;
        while (valeurLue == null) {
            try {
                valeurLue = Integer.parseInt(readString(invite));
            } catch (NumberFormatException ex) {
                System.out.println("/!\\ Erreur de saisie - Nombre entier attendu /!\\");
            }
            if (!(valeursPossibles.contains(valeurLue))) {
                System.out.println("/!\\ Erreur de saisie - Valeur non-autorisée /!\\");
                valeurLue = null;
            }
        }
        return valeurLue;
    }

    public static Integer readInteger(String invite, Integer... valeursPossibles) {
        return readInteger(invite, Arrays.asList(valeursPossibles));
    }

    public static Long readLong(String invite) {
        Long valeurLue = null;
        while (valeurLue == null) {
            try {
                valeurLue = Long.parseLong(readString(invite));
            } catch (NumberFormatException ex) {
                System.out.println("/!\\ Erreur de saisie - Nombre entier (long) attendu /!\\");
            }
        }
        return valeurLue;
    }


    public static void pause() {
        readString("--PAUSE--");
    }

}
