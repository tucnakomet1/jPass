package org.jpass.utils;

import org.jpass.crypt.dbEncryptionAES;

import java.io.File;

/** Exit program */
public class ExitProgram {

    /**
     * Exit program - encrypt data (or plaintext file and removes it)
     * @param printExit print exit message
     */
    public static void exit(boolean printExit) {
        String path = ConfigGetter.getPassFile().replace(".pass", "");
        if (new File(path).exists()) WriteFile.RemoveFile(path);

        dbEncryptionAES.encryptFileFromBytes(ConfigGetter.getData());

        if (printExit) System.out.println("Exit");
        System.exit(0);
    }
}
