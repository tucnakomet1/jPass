package org.jpass.database;

import org.jpass.utils.ConfigGetter;
import org.json.JSONArray;
import org.json.JSONObject;

/** Read from password database */
public class JsonReader {
    private static String jsonString;

    /** Constructor - load decrypted data in JSON format as String */
    public JsonReader() {
        jsonString = new String(ConfigGetter.getData());
    }

    /** Check for last id in database - by counting the length of the JSON array
     * Set lastId in ConfigGetter */
    public static void checkForLastId() {
        getInstance();  // reload the data

        JSONArray jsonArray = new JSONArray(jsonString);
        int lastId = jsonArray.length();

        ConfigGetter.setLastId(lastId);
    }

    /** Read all credentials from database and print to console */
    public static void readAll() {
        getInstance();   // reload the data
        JSONArray jsonArray = new JSONArray(jsonString);

        System.out.println("|---------------------------------------------");
        System.out.format("| %-5s| %-15s| %-15s%n", "ID", "Name", "Username");
        System.out.println("|=============================================");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            int id = jsonObject.getInt("id");
            String accountName = jsonObject.getString("accountName");
            String username = jsonObject.getString("username");

            System.out.format("| %-5s| %-15s| %-15s%n", id, accountName, username);
            System.out.println("|---------------------------------------------");
        }
    }

    /** Read specific id or account name from database and print to console
     * @param id id of the credential
     * @param uname account name of the credential
     * @param type if true, read by id, if false, read by account name
     * @return String[] with username and password
     * */
    public static String[] readSpecificIdOrUname(int id, String uname, boolean type) {
        getInstance();   // reload the data
        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            int thisId = jsonObject.getInt("id");
            String name = jsonObject.getString("accountName");

            if (type && id != thisId) continue;
            if (!type && !name.equals(uname)) continue;

            String accountName = jsonObject.getString("accountName");
            String username = jsonObject.getString("username");
            String password = jsonObject.getString("password");

            System.out.println("Credentials for account " + accountName + ".");

            return new String[] {username, password};
        }
        return new String[] {null, null};
    }

    /** Get instance of class */
    public static void getInstance() {
        new JsonReader();
    }
}
