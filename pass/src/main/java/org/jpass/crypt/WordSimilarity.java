package org.jpass.crypt;

import java.util.Arrays;

/**
 * This class is used to calculate the similarity between two words using Levenshtein algorithm.
 * <a href="https://www.wikiwand.com/en/Levenshtein_distance">Levenshtein distance link</a>
 */
public class WordSimilarity {

    /**
     * Calculate the similarity between two words using Levenshtein algorithm.
     * @param word1 first word
     * @param word2 second word
     * @return the similarity (integer) between the two words
     */
    public static int calculateLevenshteinDistance(String word1, String word2) {
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];

        for (int i = 0; i <= word1.length(); i++) {
            for (int j = 0; j <= word2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = min(dp[i - 1][j - 1] + costOfSubstitution(word1.charAt(i - 1), word2.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1);
                }
            }
        }

        return dp[word1.length()][word2.length()];
    }

    /**
     * Calculate the cost of substitution between two characters.
     * @param x first character
     * @param y second character
     * @return the cost of substitution between the two characters
     */
    private static int costOfSubstitution(char x, char y) {
        return x == y ? 0 : 1;
    }

    /**
     * Get the minimum value from a list of numbers.
     * @param numbers list of numbers
     * @return the minimum value from the list of numbers
     */
    private static int min(int... numbers) {
        return Arrays.stream(numbers).min().orElse(Integer.MAX_VALUE);
    }
}
