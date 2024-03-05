package org.jpass.crypt;

import org.jpass.utils.ReadFile;

/**
 * This class checks if the password is in the list of 10000 most common passwords from 2017.
 */
public class checkForCommonPassword {

    /**
     * This method checks if the password is in the list of 10000 most common passwords from 2017.
     * @param password - password to check
     */
    public checkForCommonPassword(String password) {
        // https://github.com/tests-always-included/password-strength/blob/master/data/common-passwords.txt
        boolean isIn = ReadFile.isInFile(password);

        if (isIn) System.out.println("This password is, beside entropy, very weak! It's actually a common password - one of top 10000 most used in 2017!");
    }
}
