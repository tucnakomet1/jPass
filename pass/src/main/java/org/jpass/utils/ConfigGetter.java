package org.jpass.utils;

import java.util.ArrayList;

/** Singleton class to get and set configuration settings */
public class ConfigGetter {
    private static String configPath = "";
    private static volatile ConfigGetter instance = null;
    private static String passFile;
    private static byte[] data;
    private static int seconds;
    private static int lastId;
    private static int[] argon2dConfig = {0, 0, 0};
    private static String[] lastRead = {"", ""};
    private static final ArrayList<String> history = new ArrayList<>();

    /* *********** getters *********** */
    /** Set pass file
     * @param passFile pass file */
    public static void setPassFile(String passFile) {
        ConfigGetter.passFile = passFile;
    }

    /** Set seconds
     * @param seconds seconds */
    public static void setSeconds(int seconds) {
        ConfigGetter.seconds = seconds;
    }

    /** Set decrypt data in bytes
     * @param data decrypted data */
    public static void setData(byte[] data) {
        ConfigGetter.data = data;
    }

    /** Set argon2d config
     * @param argon2dConfig argon2d config */
    public static void setArgon2dConfig(int[] argon2dConfig) {
        ConfigGetter.argon2dConfig = argon2dConfig;
    }

    /** Set last read
     * @param lastRead last read */
    public static void setLastRead(String[] lastRead) {
        ConfigGetter.lastRead = lastRead;
    }

    /** Set last id
     * @param lastId last id */
    public static void setLastId(int lastId) {
        ConfigGetter.lastId = lastId;
    }

    /** Set history of commands
     * @param argument command */
    public static void setHistory(String argument) {
        ConfigGetter.history.add(argument);
    }

    /** Set path to config file
     * @param configPath path to config file */
    public static void setConfigPath(String configPath) {
        ConfigGetter.configPath = configPath;
    }


    /* *********** getters *********** */
    /** Get pass file
     * @return pass file */
    public static String getPassFile() {
        return passFile;
    }

    /** Get seconds
     * @return seconds */
    public static int getSeconds() {
        return seconds;
    }

    /** get decrypted data in bytes
     * @return data */
    public static byte[] getData() {
        return data;
    }

    /** Get argon2d config
     * @return argon2d config */
    public static int[] getArgon2dConfig() {
        return argon2dConfig;
    }

    /** Get last read
     * @return last read */
    public static String[] getLastRead() {
        return lastRead;
    }

    /** Get last id
     * @return last id */
    public static int getLastId() {
        return lastId;
    }

    /** Get history of commands
     * @return history of commands */
    public static ArrayList<String> getHistory() {
        return history;
    }

    /** Get path to config file
     * @return path to config file */
    public static String getConfigPath() {
        return configPath;
    }

    /**
     * Singleton instance
     */
    public static void getInstance() {
        if (instance == null) {
            synchronized(ConfigGetter.class) {
                if (instance == null) {
                    instance = new ConfigGetter();
                }
            }
        }
    }
}
