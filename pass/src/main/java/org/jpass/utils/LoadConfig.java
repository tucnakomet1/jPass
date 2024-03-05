package org.jpass.utils;

import org.jpass.ConfigInit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/** Load config file */
public class LoadConfig {

    /** Load config file */
    public LoadConfig() {
        readConfigFile();
    }

    /**
     * Read config file and set values in ConfigGetter
     * The config file must be in the correct format (separated by "--------")
     */
    private void readConfigFile() {
        String path = ConfigGetter.getConfigPath() + "config.txt";
        String separator = "--------";
        int numberOfSep = 0;

        try {
            Scanner scanner = new Scanner(new File(path));

            while (scanner.hasNext()) {
                String line = scanner.nextLine();

                if (line.contains(separator)) numberOfSep++;
                else {
                    switch (numberOfSep) {
                        case 1 -> ConfigGetter.setPassFile(line);
                        case 2 -> ConfigGetter.setSeconds(Integer.parseInt(line));
                        case 3 -> {
                            String[] splitLine = line.split(", ");
                            ConfigGetter.setArgon2dConfig(new int[]{
                                    Integer.parseInt(splitLine[0]),
                                    Integer.parseInt(splitLine[1]),
                                    Integer.parseInt(splitLine[2])});
                        }
                    }
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Config is in incorrect format!");
            new ConfigInit();
        }
    }
}
