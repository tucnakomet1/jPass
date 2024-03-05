package org.jpass.crypt;

import org.jpass.utils.ConfigGetter;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * This class is used to encrypt and decrypt the password file.
 */
public class dbEncryptionAES {
    private static volatile dbEncryptionAES instance = null;
    private static final int iv_size = 16;

    private static String filePath;
    private static byte[] key;

    /**
     * Constructor - load the file path
     */
    public dbEncryptionAES() {
        try {
            filePath = ConfigGetter.getPassFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Encrypts the file from plain text file
     */
    public static void encryptFileFromFile() {
        Cipher cipher = getCipher(true, new byte[0]);
        byte[] iv = cipher.getIV();     // random Initialization Vector

        try (FileInputStream fis = new FileInputStream(filePath.replace(".pass", ""));
             FileOutputStream fos = new FileOutputStream(filePath);
             CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {

            fos.write(iv);

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Encrypts data from bytes to the file
     */
    public static void encryptFileFromBytes(byte[] data) {
        Cipher cipher = getCipher(true, new byte[0]);
        byte[] iv = cipher.getIV();     // random Initialization Vector
        data = concatArr(iv, data);

        try {
            Files.write(Paths.get(filePath), cipher.doFinal(data));
        } catch (IllegalBlockSizeException | BadPaddingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Concatenates two byte arrays
     * @param arr1 first byte array
     * @param arr2 second byte array
     * @return concatenated byte array
     */
    private static byte[] concatArr(byte[] arr1, byte[] arr2) {
        byte[] concat = new byte[arr1.length + arr2.length];

        System.arraycopy(arr1, 0, concat, 0, arr1.length);      // copy arr1 to concat
        System.arraycopy(arr2, 0, concat, arr1.length, arr2.length);    // copy arr2 to concat

        return concat;
    }

    /**
     * Decrypts the file to bytes
     */
    public static void decryptFileToBytes() {
        try {
            byte[] encryptedFile = Files.readAllBytes(Path.of(filePath));
            byte[] iv = Arrays.copyOfRange(encryptedFile, 0, iv_size);  // checking iv - 16b for AES-256 in CBC mode

            encryptedFile = Arrays.copyOfRange(encryptedFile, iv_size, encryptedFile.length); // remove iv from encrypted file

            Cipher cipher = getCipher(false, iv);
            ConfigGetter.setData(cipher.doFinal(encryptedFile));
        } catch (IOException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the cipher
     * @param encrypt boolean value to encrypt or decrypt
     * @param iv initialization vector
     * @return cipher
     */
    private static Cipher getCipher(boolean encrypt, byte[] iv) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            if (encrypt) cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            else cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }

        return cipher;
    }

    /**
     * Gets the instance
     * @param bkey byte array key
     */
    public static void getInstance(byte[] bkey) {
        key = bkey;

        if (instance == null) {
            synchronized(dbEncryptionAES.class) {
                if (instance == null) {
                    instance = new dbEncryptionAES();
                }
            }
        }
    }
}
