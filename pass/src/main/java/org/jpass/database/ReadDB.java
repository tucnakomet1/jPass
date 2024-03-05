package org.jpass.database;

import org.jpass.utils.ConfigGetter;

/** Read from password database */
public class ReadDB {

    /** Read all credentials from database */
    public static void readAll() {
        JsonReader.readAll();
    }

    /**
     * Read specific id from database
     * @param id id of the credential
     * @param hide boolean to hide password
     */
    public static void readSpecificId(int id, boolean hide) {
        String[] credentials = JsonReader.readSpecificIdOrUname(id, "", true);
        readBySpecific(credentials, hide);
    }

    /**
     * Read specific account name from database
     * @param uname account name of the credential
     * @param hide boolean to hide password
     */
    public static void readSpecificName(String uname, boolean hide) {
        String[] credentials = JsonReader.readSpecificIdOrUname(0 , uname, false);
        readBySpecific(credentials, hide);
    }

    /**
     * Read specific credentials from database and print to console
     * @param credentials String[] with username and password
     * @param hide boolean to hide password
     */
    private static void readBySpecific(String[] credentials, boolean hide) {
        if (credentials[0] != null) {
            ConfigGetter.setLastRead(credentials);

            String username = credentials[0];
            String password = credentials[1];

            if (hide) {
                password = "*".repeat(password.length());
            }

            System.out.println("Username: \t\t" + username);
            System.out.println("Password: \t\t" + password);
        } else System.out.println("This id does not exist! Check 'ls' for listing all credentials.");
    }
}
