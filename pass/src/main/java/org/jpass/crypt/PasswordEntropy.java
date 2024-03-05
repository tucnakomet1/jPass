package org.jpass.crypt;

import java.util.HashMap;
import java.util.Map;


/**
 * This class is used to calculate the Shannon's entropy of a password.
 * Inspiration: <a href="https://www.wikiwand.com/en/Entropy_(information_theory)">Wikipedia Link</a>
 * Inspiration: <a href="http://tests-always-included.github.io/password-strength/">Github Link</a>
 */
public class PasswordEntropy {

    /**
     * This method is used to calculate the entropy of a password.
     * @param pass The password to calculate the entropy of.
     * @return The entropy of the password. (double)
     */
    public static double countEntropy(String pass) {
        double sum = 0.0;
        int passLength = pass.length();
        Map<Character, Integer> freq = countFrequencies(pass, passLength);

        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            int value = entry.getValue();

            double score = (double) value / passLength;
            sum -= score * (Math.log(score) / Math.log(2));
        }

        return sum * passLength;
    }

    /**
     * This method is used to return the word status of the entropy of a password.
     * Source: <a href="https://github.com/tests-always-included/password-strength/blob/master/doc/strength-levels.md">Github Link</a>
     * @param entropy The entropy of the password.
     * @return The status of the entropy of the password. (String)
     */
    public static String statusEntropy(double entropy) {
        if (entropy < 32) {
            return "Very weak! Keep out a typical attacker for minutes.";
        } else if (entropy < 48) {
            return "Weak! Crackable by a typical home computer in a week.";
        } else if (entropy < 64) {
            return "Reasonable! A specialized computer could get this in one year.";
        } else if (entropy < 80) {
            return "Strong! Resistant to a large, coordinated attack (botnet) for over a year.";
        } else {
            return "Very strong! Nearly impossible to brute force, given more than all of the computing power in the world, optimized algorithms, specialized hardware and a thousand years.";
        }
    }

    /**
     * This method is used to count the frequencies of the characters in a password.
     * @param password The password to count the frequencies of.
     * @param passLength The length of the password.
     * @return A map of the characters and their frequencies. (Map)
     */
    private static Map<Character, Integer> countFrequencies(String password, int passLength) {
        Map<Character, Integer> charFrequencies = new HashMap<>();

        for (int i = 0; i < passLength; i++) {
            char c = password.charAt(i);

            if (charFrequencies.containsKey(c)) {
                charFrequencies.put(c, charFrequencies.get(c) + 1);
            } else {
                charFrequencies.put(c, 1);
            }
        }

        return charFrequencies;
    }
}
