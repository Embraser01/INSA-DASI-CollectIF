package fr.insalyon.dasi.collectif.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author DASI Team
 */
public class Saisie {

    public static Date readDate(String dateFormat) {
        Scanner scanner = new Scanner(System.in);

        DateFormat format = new SimpleDateFormat(dateFormat);
        Date date = null;
        while (date == null) {
            String line = scanner.nextLine();
            try {
                date = format.parse(line);
            } catch (ParseException e) {
                System.out.println("Sorry, that's not valid. Please try again.");
            }
        }
        return date;
    }
}
