package org.jpass.crypt;

import java.util.Random;

/**
 * Generate a password with a given length and special characters
 */
public class generatePassword {
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%&*()_+-=[]|,./?><";

    /**
     * Generate a password with a given length and special characters
     * @param len length of the password
     * @param specialChars include special characters
     * @return generated password
     */
    public static String generate(int len, boolean specialChars) {

        StringBuilder pass = new StringBuilder(len);
        Random random = new Random(System.nanoTime());

        String[] characters;
        if (specialChars) characters = new String[] {LOWER, UPPER, DIGITS, SPECIAL};
        else characters = new String[] {LOWER, UPPER, DIGITS};

        for (int i = 0; i < len; i++) {
            int ran = random.nextInt(characters.length);
            String character = characters[ran];

            int index = random.nextInt(character.length());
            pass.append(character.charAt(index));
        }
        return pass.toString();
    }
}
