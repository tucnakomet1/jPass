package org.jpass;

import org.jpass.crypt.dbEncryptionAES;
import org.jpass.crypt.passEncryptionArgon2;
import org.jpass.database.JsonReader;
import org.jpass.utils.ConfigGetter;
import org.jpass.utils.ExitProgram;
import org.jpass.utils.LoadConfig;
import org.jpass.utils.PasswordInput;

import java.io.File;

/** Main class - entry point of the application */
public class Main {

    /** Main method - entry point of the application
     * @param args command line arguments */
    public static void main(String[] args) {
        // initialize config getter and set path to config file (also check if config file exists)
        ConfigGetter.getInstance();

        String configPath = getPathConfig();
        ConfigGetter.setConfigPath(configPath);

        if (!new File(configPath + "config.txt").exists()) new ConfigInit();

        // load config file
        new LoadConfig();

        // encrypt password and verify hash
        System.out.println("Login.");
        String password = PasswordInput.getPassword();
        passEncryptionArgon2.getInstance();

        byte[] b_enc = passEncryptionArgon2.encryptPassBytes(password);
        String enc = passEncryptionArgon2.encryptPass(b_enc);   // convert encrypted byte[] password to string

        if (passEncryptionArgon2.verifyPassword(enc)) {  // passwords does match, decrypt data now
            dbEncryptionAES.getInstance(b_enc);
            dbEncryptionAES.decryptFileToBytes();
        } else {
            System.out.println("Passwords don't match!");
            System.exit(0);
        }

        // now when the data is decrypted we can continue using parsing arguments or starting UI
        JsonReader.checkForLastId();

        if (args.length > 0) new ParseArgs(args);
        else ParseArgs.startUI();

        ExitProgram.exit(false);
    }

    /** Create path to config file in user's home directory
     * @return path to config file */
    private static String getPathConfig() {
        String home = System.getProperty("user.home");
        String configDir = ".config" + File.separator + "jpass" + File.separator;

        return home + File.separator + configDir;
    }
}