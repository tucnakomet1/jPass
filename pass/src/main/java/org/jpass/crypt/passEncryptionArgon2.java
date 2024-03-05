package org.jpass.crypt;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.jpass.utils.ConfigGetter;
import org.jpass.utils.ReadFile;

import java.nio.charset.StandardCharsets;

/** Class to encrypt password using Argon2d algorithm */
public class passEncryptionArgon2 {
    private static volatile passEncryptionArgon2 instance = null;
    private static Argon2Parameters parameters;

    /** Constructor to set parameters for Argon2d algorithm */
    public passEncryptionArgon2() {
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_d);
        int[] config = ConfigGetter.getArgon2dConfig();

        builder.withIterations(config[0]);
        builder.withMemoryAsKB(config[1]);
        builder.withParallelism(config[2]);

        parameters = builder.build();
    }

    /**
     * Encrypt password to bytes
     * @param password password to encrypt
     * @return encrypted password as bytes
     */
    public static byte[] encryptPassBytes(String password) {
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8); // convert to bytes

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(parameters);

        byte[] hash = new byte[32];         // output field of bytes
        generator.generateBytes(passwordBytes, hash, 0, hash.length);       // encryption

        return hash;
    }

    /**
     * Encrypt password to string
     * @param password bytes password to encrypt
     * @return encrypted password as string
     */
    public static String encryptPass(byte[] password) {
        return bytesToHex(password);
    }

    /** Convert bytes to hex
     * @param bytes bytes to convert
     * @return converted password as string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        for (byte b : bytes)
            sb.append(String.format("%02x", b));

        return sb.toString();
    }

    /** Verify password - compare hash saved in .config/jpass/.secret file with password
     * @param password password to verify
     * @return boolean value of password verification
     */
    public static boolean verifyPassword(String password) {
        String hash = ReadFile.read(ConfigGetter.getConfigPath() + ".secret", false);
        return password.equals(hash);
    }

    /** Get instance of class */
    public static void getInstance() {
        if (instance == null) {
            synchronized(passEncryptionArgon2.class) {
                if (instance == null) {
                    instance = new passEncryptionArgon2();
                }
            }
        }
    }
}
