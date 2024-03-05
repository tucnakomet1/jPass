package org.jpass.database;

import org.jpass.utils.ConfigGetter;
import org.json.JSONArray;
import org.json.JSONObject;

/** Edit or remove credentials from database by id or name */
public class JsonEditRemove {
    private static String jsonString;

    /** Constructor -  load decrypted data in JSON format as String  */
    public JsonEditRemove() {
        //filePath = ConfigGetter.getPassFile().replace(".pass", "");
        jsonString = new String(ConfigGetter.getData());
    }

    /** Edit credentials by id or name (one of them has to be 0 or null)
     * @param id id of the credential
     * @param name name of the credential
     * @param args arguments to edit
     */
    public static void editByIdOrName(int id, String name, String[] args) {
        getInstance();   // reload the file
        JSONArray jsonArray = new JSONArray(jsonString);
        String replacement = "accountName";     // default replacement

        switch (args[0]) {  // change the replacement by input value of args[0]
            case "u" -> replacement = "username";
            case "p" -> replacement = "password";
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int currentId = jsonObject.getInt("id");
            String currentName = jsonObject.getString("accountName");

            if ((id != 0 && currentId == id) || currentName.equals(name)) {
                jsonObject.put(replacement, args[1]);
                break;
            }
        }

        // reload edited data
        ConfigGetter.setData(jsonArray.toString(2).getBytes());
    }

    /** Remove credentials by id or name (one of them has to be 0 or null)
     * @param id id of the credential
     * @param name name of the credential
     */
    public static void removeByIdOrName(int id, String name) {
        getInstance();   // reload the file
        JSONArray jsonArray = new JSONArray(jsonString);
        boolean sub = false;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int currentId = jsonObject.getInt("id");
            String currentName = jsonObject.getString("accountName");

            if ((id != 0 && currentId == id) || currentName.equals(name)) {
                jsonArray.remove(i);
                sub = true;
                i--;
                continue;
            }

            if (sub) jsonObject.put("id", currentId-1); // if removed, decrement id by 1 for all next credentials
        }

        // reload edited data
        ConfigGetter.setData(jsonArray.toString(2).getBytes());
        ConfigGetter.setLastId(ConfigGetter.getLastId() - 1);
    }

    /** Get instance of class */
    public static void getInstance() {
        new JsonEditRemove();
    }
}
