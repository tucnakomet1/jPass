package org.jpass;

import org.jpass.crypt.PasswordEntropy;
import org.jpass.crypt.checkForCommonPassword;
import org.jpass.crypt.generatePassword;
import org.jpass.database.IterateHistory;
import org.jpass.database.JsonEditRemove;
import org.jpass.database.JsonWriter;
import org.jpass.database.ReadDB;
import org.jpass.utils.*;

import java.io.File;
import java.util.Scanner;

/** Parse command line arguments */
public class ParseArgs {

    /** Constructor - parse command line arguments
     * @param args command line arguments */
    public ParseArgs(String[] args) {
        parse(args, true);
    }

    /** Parse command line arguments - commands help, version, config, add, gen, rm, edit, id, uname, ls, c, ui, exit
     * @param args command line arguments
     * @param ui boolean to start UI */
    public static void parse(String[] args, boolean ui) {
        label:
        for (int i = 0; i < args.length; i++) {
            String argument = args[i].replaceAll("-", "").replaceAll("\\s+", "");

            if (argument.equals("ui") && ui)
                startUI();

            switch (argument) {
                case "help", "h" -> printHelp();
                case "version", "v" -> printVersion();
                case "ls" -> listAll();
                case "exit" -> ExitProgram.exit(true);
                case "" -> {}
                case "add" -> {
                    if (args.length < 2) {
                        System.out.println("Enter the name of new field of credentials. (Example: Facebook/ Bank/ ...)");
                        break;
                    }
                    argument = args[++i].replaceAll("-", "").replaceAll("\\s+", "");
                    addCredentials(argument);
                }
                case "gen" -> {
                    boolean specialChars = true;
                    if (args.length < 3) {
                        System.out.println("Enter the name of new field of credentials and length of password. (Example: Facebook/ Bank/ ...)");
                        break;
                    }
                    argument = args[++i].replaceAll("-", "").replaceAll("\\s+", "");
                    int len = Integer.parseInt(args[++i].replaceAll("-", "").replaceAll("\\s+", ""));

                    if (i+1 < args.length) {
                        String y = args[++i].replaceAll("-", "").replaceAll("\\s+", "");
                        if (y.equals("n")) specialChars = false;
                    }

                    addCredentials(argument, len, specialChars);
                }
                case "rm" -> {
                    if (args.length < 2) {
                        System.out.println("Enter the id or name of credentials to be removed. (Example: Facebook/ Bank/ ...)");
                        break;
                    }
                    argument = args[++i].replaceAll("-", "").replaceAll("\\s+", "");
                    removeCredentials(argument);
                }
                case "edit" -> {
                    if (args.length < 3) {
                        System.out.println("Enter the id or name of credentials to be edited! And enter type: [(n)name | (u)username | (p)password]. Type 'help' for more info.");
                        break;
                    }
                    argument = args[++i].replaceAll("-", "").replaceAll("\\s+", "");

                    String type = args[++i].replaceAll("-", "").replaceAll("\\s+", "");
                    editCredentials(argument, type);
                }
                case "config" -> {
                    if (i + 1 < args.length) {
                        argument = args[++i].replaceAll("-", "").replaceAll("\\s+", "");
                        initPass(argument);
                    } else System.out.println("Enter file path.");
                }
                case "id" -> {
                    int id = 0;
                    boolean y = false;

                    if (args.length < 2) {
                        System.out.println("Enter specific integer value of id! Type 'help' for more details.");
                        break;
                    }

                    try {
                        id = Integer.parseInt(args[++i]);
                    } catch (NumberFormatException ex) {
                        System.out.println("The id must be integer!");
                    }

                    if (id != 0 && i + 1 < args.length) {
                        argument = args[++i].replaceAll("-", "").replaceAll("\\s+", "");
                        if (argument.equals("y"))
                            y = true;
                    }

                    printId(id, y);
                }
                case "uname" -> {
                    boolean y = false;

                    if (args.length < 2) {
                        System.out.println("Enter the name! Type 'help' for more details.");
                        break;
                    }

                    String name = args[++i];

                    if (i + 1 < args.length) {
                        argument = args[++i].replaceAll("-", "").replaceAll("\\s+", "");
                        if (argument.equals("y"))
                            y = true;
                    }

                    printName(name, y);
                }
                case "c" -> {
                    if (args.length < 3) {
                        System.out.println("Username or password? And enter specific integer value of id! Type 'help' for more details.");
                        continue ;
                    }

                    argument = args[++i].replaceAll("-", "").replaceAll("\\s+", "");
                    boolean password = false;

                    if (argument.equals("p")) {
                        password = true;
                    } else if (!argument.equals("u")) {
                        System.out.println("Invalid input! Type 'help' for more details.");
                        break label;
                    }

                    int id = 0;
                    try {
                        id = Integer.parseInt(args[++i]);
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                        System.out.println("The id must be integer!");
                    }

                    copyToClipboard(id, password);
                }
                default -> System.out.println("Command not found! Type 'help' for usage.");
            }
        }
    }

    /** Start UI - command line interface */
    public static void startUI() {
        new KeyListenerUtil();  // start key listener for clipboard

        // listen the system input
        Scanner scanner = new Scanner(System.in);
        String[] input;
        do {
            System.out.print("> ");
            String strInput = scanner.nextLine();

            ConfigGetter.setHistory(strInput);
            IterateHistory.updateIndex();

            input = strInput.split(" ");
            parse(input, false);
        } while (input.length > 0);

        scanner.close();
    }

    /** Print specific id from database
     * @param id id of the credential
     * @param hide boolean to hide password */
    private static void printId(int id, boolean hide) {
        ReadDB.readSpecificId(id, !hide);
    }

    /** Print specific uname from database
     * @param uname id of the credential
     * @param hide boolean to hide password */
    private static void printName(String uname, boolean hide) {
        ReadDB.readSpecificName(uname, !hide);
    }

    /** Copy to clipboard specific credential from database
     * @param id id of the credential
     * @param password boolean to copy password */
    private static void copyToClipboard(int id, boolean password) {
        ReadDB.readSpecificId(id, !password);

        String credToCopy = ConfigGetter.getLastRead()[0];
        if (password) credToCopy = ConfigGetter.getLastRead()[1];

        CopyToClipboard.copy(credToCopy);
    }

    /** List all credentials from database */
    private static void listAll() {
        ReadDB.readAll();
    }

    /** Replace line in config file (number of seconds or path to password file)
     * @param argument path to password file or number of seconds */
    private static void initPass(String argument) {
        int seconds = -1;
        String path = "";

        // check if argument is a number or string
        if (argument.matches("-?\\d+(\\.\\d+)?")) seconds = Integer.parseInt(argument);
        else path = argument;

        if (!path.contains(".pass") && seconds < 0 )    // check for invalid input - must contain .pass or number
            System.out.println("Invalid input! The password file must contain '.pass' extension. \n" +
                "If you want to create new .pass file, remove config.txt and restart the program.");
        else if (path.contains(".pass")) {              // check if file exists
            if (new File(path).exists()) {
                addToConfig(path, 1);
                System.out.println("The path to password file has been updated. Restart the program.");
                ExitProgram.exit(true);
            }
            else System.out.println("Invalid input! The password file must exist! \n" +
                        "If you want to create new .pass file, remove config.txt and restart the program.");
        } else
            addToConfig(String.valueOf(seconds), 2);
    }

    /** Add path to password file into the config file
     * @param text path to password file
     * @param place place of separator in the config file */
    private static void addToConfig(String text, int place) {
        WriteFile.write(ConfigGetter.getConfigPath() + "config.txt", text, place);
    }

    /** Add credentials to the password file - username and password (also check for common password)
     * @param argument name of the credentials */
    private static void addCredentials(String argument) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        String password = PasswordInput.getPassword();
        new checkForCommonPassword(password);

        JsonWriter.write(argument, username, password);
    }

    /** Add credentials to the password file - username and generate random password
     * @param argument name of the credentials
     * @param len length of the password
     * @param specialChars boolean to include special characters */
    private static void addCredentials(String argument, int len, boolean specialChars) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        String password = generatePassword.generate(len, specialChars);

        double entropy = PasswordEntropy.countEntropy(password);
        System.out.println("The entropy of the password is: " + entropy + " bits");
        System.out.println("The password is " + PasswordEntropy.statusEntropy(entropy));

        JsonWriter.write(argument, username, password);
    }

    /** Edit credentials in the password file by id or name - name, username or password
     * @param argument id or name of the credentials
     * @param type type of the credentials */
    private static void editCredentials(String argument, String type) {
        int id = 0;
        String name = null;
        String[] args = new String[2];

        if (argument.matches("-?\\d+(\\.\\d+)?"))
            id = Integer.parseInt(argument);
        else name = argument;

        Scanner scanner = new Scanner(System.in);

        switch (type) {
            case "n", "name" -> {
                args[0] = "n";
                System.out.print("Enter new name: ");
                args[1] = scanner.nextLine();
            }
            case "u", "username" -> {
                args[0] = "u";
                System.out.print("Enter new username: ");
                args[1] = scanner.nextLine();
            }
            case "p", "password" -> {
                boolean useSpecials = true;
                String password;
                String[] input;

                args[0] = "p";

                System.out.print("Generate or enter new password (g/e): ");
                String gen = scanner.nextLine();

                if (gen.equals("g")) {
                    System.out.print("Enter new length of password and if to use specials (in format '<int>, <y|n>'): ");
                    String line = scanner.nextLine();
                    if (line.contains(", ")) input = line.split(", ");
                    else input = line.split("\\s+");

                    int len = Integer.parseInt(input[0]);
                    if (input.length > 1) useSpecials = input[1].equals("y");

                    password = generatePassword.generate(len, useSpecials);
                } else
                    password = PasswordInput.getPassword();
                new checkForCommonPassword(password);
                args[1] = password;
            }
        }

        JsonEditRemove.editByIdOrName(id, name, args);
    }

    /** Remove credentials from the password file by id or name
     * @param argument id or name of the credentials */
    private static void removeCredentials(String argument) {
        int id = 0;
        String name = null;

        if (argument.matches("-?\\d+(\\.\\d+)?"))
            id = Integer.parseInt(argument);
        else name = argument;

        JsonEditRemove.removeByIdOrName(id, name);
    }

    /** Print version of the application */
    private static void printVersion() {
        ReadFile.read(ConfigGetter.getConfigPath() + "version.txt", true);
    }

    /** Print help */
    private static void printHelp() {
        System.out.println("Simple Java password manager.\n\nUsage: [h/help], [v/version], [config], [add], [gen], [rm], [edit], [id], [uname], [ls], [c], [ui], [exit]");

        System.out.println("  Basic:");
        System.out.println("\th | help\t\t\t\t\t Show this text.");
        System.out.println("\tv | version\t\t\t\t\t Show version information.");
        System.out.println("\tconfig <path | seconds>\t\t Replace in config file path to password file or number of seconds.");
        System.out.println("\tadd <uname>\t\t\t\t\t Add username and password.");
        System.out.println("\tgen <uname> <len> [n] \t\t Add username and generate random password. Length is necessary. Optional argument 'n' turns off special characters.");
        System.out.println("\trm <uname | id>\t\t\t\t Delete username and password.");
        System.out.println("\tedit <uname | id> [n|u|p]\t Edit account name <n | name>, username <u | username> or password <p | password>.");

        System.out.println("\n  Info:");
        System.out.println("\tid <int> [y]\t\t\t\t Show username and (hidden) password information - to show plaintext password use with argument y. (example 'id 2 y')");
        System.out.println("\tuname <string> [y]\t\t\t Show username and (hidden) password information - to show plaintext password use with argument y.");
        System.out.println("\tls\t\t\t\t\t\t\t Shows the content of the entire pass file (ids, account names and usernames)");
        System.out.println("\tc [u | p] <id>\t\t\t\t Copy to clipboard. Use with argument u=username or p=password and id value. (example 'c u 2')");
    }
}
