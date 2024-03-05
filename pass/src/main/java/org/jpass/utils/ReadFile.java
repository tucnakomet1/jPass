package org.jpass.utils;

import org.jpass.ConfigInit;
import org.jpass.crypt.WordSimilarity;

import java.io.*;
import java.util.Scanner;

/** Read file */
public class ReadFile {

    /**
     * Read file and return string
     * @param file file to read
     * @param print also print file content
     * @return file content
     */
    public static String read(String file, boolean print) {
        StringBuilder str = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(file));

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (print) System.out.println(line);
                str.append(line);
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } return str.toString();
    }

    /**
     * Check if string is in file (used for checking if password is in file)
     * Also check for similar strings using Levenshtein distance (if distance is less than 4)
     * @param match string to match
     * @return boolean value of string existence
     */
    public static boolean isInFile(String match) {
        InputStream is = ConfigInit.class.getResourceAsStream("/common-passwords.txt");
        try {
            assert is != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.equals(match)) return true;
                    if (WordSimilarity.calculateLevenshteinDistance(line, match) < 4) return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file! " + e.getMessage());
        }
        return false;
    }
}
