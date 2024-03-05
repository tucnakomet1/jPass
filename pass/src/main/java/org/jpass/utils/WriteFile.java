package org.jpass.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

/** This class is used to write into a file */
public class WriteFile {

    /**
     * Create a file
     * @param filePath path to the file
     */
    public static void create(String filePath) {
        Path newFilePath = Paths.get(filePath);
        try {
            Files.createFile(newFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Write into a file
     * @param filePath path to the file
     * @param text text to write into the file
     */
    public static void write(String filePath, String text) {
        try (FileWriter myWriter = new FileWriter(filePath)) {
            myWriter.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Append text into a config file
     * Make copy (.copy file), read from copy and write into original file, replace specific line, delete copy
     * @param filePath path to the file
     * @param text text to append into the file
     */
    public static void write(String filePath, String text, int split) {
        CopyFile(filePath);

        int counter = 0;
        boolean here = false;

        BufferedWriter writer;
        try (FileWriter fw = new FileWriter(filePath, false); Scanner scanner = new Scanner(new File(filePath + ".copy"))) {
            writer = new BufferedWriter(fw);

            while (scanner.hasNext()) {
                if (here) { // append text -> "replace" specific line
                    here = false;
                    scanner.nextLine();

                    writer.write(text);
                    writer.newLine();
                    continue;
                }
                String line = scanner.nextLine();

                writer.write(line);
                writer.newLine();

                if (line.contains("--------")) {
                    if (++counter == split) {
                        here = true;
                    }
                }
            }
            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        RemoveFile(filePath + ".copy");
    }

    /**
     * Copy a file and save it as .copy file - used as a helper in append method
     * @param filePath path to the file
     */
    public static void CopyFile(String filePath) {
        CopyFile(filePath, filePath + ".copy");
    }

    /**
     * Copy a file
     * @param filePath String path to the file
     * @param newFilePath String path to the new file
     */
    public static void CopyFile(String filePath, String newFilePath) {
        Path oldFile = Paths.get(filePath);
        CopyFile(oldFile, newFilePath);
    }

    /**
     * Copy a file
     * @param filePath Path to the file
     * @param newFilePath String path to the new file
     */
    public static void CopyFile(Path filePath, String newFilePath) {
        Path newFile = Paths.get(newFilePath);
        try {
            Files.copy(filePath, newFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Remove a file
     * @param filePath path to the file
     */
    public static void RemoveFile(String filePath) {
        Path path = Paths.get(filePath);
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
