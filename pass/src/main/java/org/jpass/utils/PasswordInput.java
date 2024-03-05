package org.jpass.utils;

import org.jpass.crypt.PasswordEntropy;

import java.io.Console;
import java.util.Scanner;

/** Get password from user input */
public class PasswordInput {

    /**
     * Get password from user input and calculate entropy
     * The input is hidden when using console (works only in terminal - usually not in IDE)
     * @return password as string
     */
    public static String getPassword() {
        String password;

        Console console = System.console();

        if (console == null) {
            System.out.println("Unable to fetch console! Using normal scanner instead.");
            password = normalScanner();
        } else {
            char[] passwordArray = console.readPassword("Enter password: ");
            password = new String(passwordArray);
        }

        double entropy = PasswordEntropy.countEntropy(password);
        System.out.println("The entropy of the password is: " + entropy + " bits");
        System.out.println("The password is " + PasswordEntropy.statusEntropy(entropy));

        return password;
    }

    /**
     *  Get password from user input using normal scanner
     * @return password as string
     */
    private static String normalScanner() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter password: ");
        return scanner.nextLine();
    }
}
