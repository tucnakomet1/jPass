package org.jpass;

import org.jpass.crypt.dbEncryptionAES;
import org.jpass.crypt.passEncryptionArgon2;
import org.jpass.utils.*;

import java.io.*;
import java.util.Scanner;

/** Initialize config file */
public class ConfigInit {
    private static String pathConfig = "";
    private static boolean encrypt = false;

    /** Constructor - initialize path to config file and check for existing config
     * If config file doesn't exist, create it (also create version file, move wordlist into config directory and create secret file) */
    public ConfigInit() {
        pathConfig = ConfigGetter.getConfigPath() + "config.txt";

        boolean exists = checkForExistingConfig();
        if (!exists) {
            createConfig();
            createVersion();
            createSecret();
            System.out.println("Created config file at: " + pathConfig);
        } else {
            System.out.println("Config file already exists at: " + pathConfig);
            System.out.println("If you want to change the settings, please edit the file manually or delete it and restart the application.");
        }
    }

    /** Create version file and save it to config directory */
    private void createVersion() {
        String pathVersion = ConfigGetter.getConfigPath() + "version.txt";
        WriteFile.create(pathVersion);
        WriteFile.write(pathVersion, "v0.0.1");
    }

    /** Create secret file and encrypt password database */
    private void createSecret() {
        String pathSecret = ConfigGetter.getConfigPath() + ".secret";
        WriteFile.create(pathSecret);

        System.out.println("Please enter the password to encrypt your password database.");
        String password = PasswordInput.getPassword();

        new LoadConfig();

        // encrypt password and verify hash
        passEncryptionArgon2.getInstance();

        byte[] b_enc = passEncryptionArgon2.encryptPassBytes(password);
        String enc = passEncryptionArgon2.encryptPass(b_enc);

        if (encrypt) {  // only if new password file was created
            dbEncryptionAES.getInstance(b_enc);
            dbEncryptionAES.encryptFileFromFile();
        }

        WriteFile.write(pathSecret, enc);
    }

    /** Create whole config file */
    private void createConfig() {
        createDirectory();

        String pathPassFile = editFilePath();
        int seconds = editCopyTime();
        int[] argon2dConfig = editArgon2Settings();

        // write to file
        WriteFile.create(pathConfig);
        WriteFile.write(pathConfig, "This is config file\n" +
                "-------- path to file with passwords --------\n" +
                        pathPassFile + "\n" +
                "-------- number of seconds --------\n" +
                        seconds + "\n" +
                "-------- argon2d config (iterations, memory in KB, threads) --------\n" +
                        argon2dConfig[0] + ", " + argon2dConfig[1] + ", " + argon2dConfig[2] + "\n" +
                "-------- end of file --------\n");
    }

    /** Create directory for config file */
    private void createDirectory() {
        String defaultPath = System.getProperty("user.home") + File.separator + ".config";
        File directory = new File(defaultPath);
        if (!directory.exists()) {
            if (!directory.mkdir()) {
                System.out.println("Couldn't create directory: " + defaultPath);
                ExitProgram.exit(true);
            }
        }

        defaultPath = defaultPath + File.separator + "jpass";
        directory = new File(defaultPath);
        if (!directory.exists()) {
            if (!directory.mkdir()) {
                System.out.println("Couldn't create directory: " + defaultPath);
                ExitProgram.exit(true);
            }
        }
    }

    /** Check if config file exists
     * @return boolean value of file existence */
    private static boolean checkForExistingConfig() {
        return checkForExistingFile(pathConfig);
    }

    /** Check if file exists
     * @param path path to file
     * @return boolean value of file existence */
    private static boolean checkForExistingFile(String path) {
        File file = new File(path);
        return file.exists();
    }

    /** Add path to file with passwords (.pass file)
     * @return path to file with passwords */
    public static String editFilePath() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter path to file with passwords: ");
        String passPath = scanner.nextLine();

        if (passPath.contains(".pass") && !checkForExistingFile(passPath)) {    // the file doesn't exist yet
            encrypt = true;
            WriteFile.create(passPath.replace(".pass", ""));
            WriteFile.write(passPath.replace(".pass", ""), "[]");
        } else if (!passPath.contains(".pass")){
            encrypt = true;
            if (!checkForExistingFile(passPath)) {
                WriteFile.create(passPath);
                WriteFile.write(passPath, "[]");
            }

            passPath = passPath + ".pass";
        }

        return passPath;
    }

    /** Edit number of seconds for copying password before it's deleted from clipboard
     * @return number of seconds */
    public static int editCopyTime() {
        int time = 5;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of seconds for copying password before it's deleted from clipboard (default is 5): ");

        String strTime = scanner.nextLine();

        if (!strTime.isEmpty()) {
            try {
                time = Integer.parseInt(strTime);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Using default value.");
            }
        }

        return time;
    }

    /** Create Argon2d settings for hashing passwords (iterations, memory in KB, threads)
     * @return array of Argon2d settings */
    public static int[] editArgon2Settings() {
        int[] settings = {10, 65536, 1};
        int cores = Runtime.getRuntime().availableProcessors();
        if (cores > 1) settings[2] = cores/2;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of iterations for Argon2d (default is 10): ");
        String strIterations = scanner.nextLine();
        if (!strIterations.isEmpty()) {
            try {
                settings[0] = Integer.parseInt(strIterations);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Using default value.");
            }
        }

        System.out.print("Enter amount of memory in KB for Argon2d (default is 65536): ");
        String strMemory = scanner.nextLine();
        if (!strMemory.isEmpty()) {
            try {
                settings[1] = Integer.parseInt(strMemory);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Using default value.");
            }
        }

        System.out.print("Enter number of threads for Argon2d (default is " + (cores/2) + "): ");
        String strThreads = scanner.nextLine();
        if (!strThreads.isEmpty()) {
            try {
                settings[2] = Integer.parseInt(strThreads);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Using default value.");
            }
        }

        return settings;
    }

}