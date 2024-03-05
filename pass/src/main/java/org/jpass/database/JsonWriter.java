package org.jpass.database;

import org.jpass.utils.ConfigGetter;
import org.json.JSONArray;
import org.json.JSONObject;

/** Write to password database */
public class JsonWriter {
    private static String jsonString;

    /** Constructor - load decrypted data in JSON format as String  */
    public JsonWriter() {
        jsonString = new String(ConfigGetter.getData());
    }

    /** Write new credentials to database - also add with new id
     * @param name account name
     * @param username username
     * @param password password */
    public static void write(String name, String username, String password) {
        getInstance();  // reload the data

        int lastId = ConfigGetter.getLastId();
        ConfigGetter.setLastId(lastId + 1);

        JSONObject newObject = new JSONObject();
        newObject.put("id", lastId + 1);
        newObject.put("accountName", name);
        newObject.put("username", username);
        newObject.put("password", password);

        JSONArray jsonArray = new JSONArray(jsonString);
        jsonArray.put(newObject);

        // reload edited data
        ConfigGetter.setData(jsonArray.toString(2).getBytes());
    }

    /** Get instance of class */
    public static void getInstance() {
        new JsonWriter();
    }
}
