/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fileencryptionapp;

/**
 * AES256 encryption and decryption class.
 * 
 * The encryptToFile and decryptFromFile methods in this class are adapted from:
 * HowToDoInJava. (n.d.). AES 256 Encryption and Decryption in Java. 
 * Retrieved from https://howtodoinjava.com/java/java-security/aes-256-encryption-decryption/
 */

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

public class AES256 {

    private static final int KEY_LENGTH = 256;
    private static final int ITERATION_COUNT = 65536;

    public static void encryptToFile(String strToEncrypt, String secretKey, String salt, String outputFilePath) {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] iv = new byte[16];
            secureRandom.nextBytes(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), ITERATION_COUNT, KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);

            byte[] cipherText = cipher.doFinal(strToEncrypt.getBytes("UTF-8"));
            byte[] encryptedData = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, encryptedData, 0, iv.length);
            System.arraycopy(cipherText, 0, encryptedData, iv.length, cipherText.length);

            String encryptedContent = Base64.getEncoder().encodeToString(encryptedData);

            FileTemplate fileTemplate = new FileTemplate(encryptedContent, salt);

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(outputFilePath), fileTemplate);

            System.out.println("Encryption successful! Encrypted content written to file: " + outputFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String decryptFromFile(String secretKey, String inputFilePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            FileTemplate fileTemplate = mapper.readValue(new File(inputFilePath), FileTemplate.class);
            String encryptedContent = fileTemplate.getEncryptedContent();
            String salt = fileTemplate.getSalt();

            byte[] encryptedData = Base64.getDecoder().decode(encryptedContent);

            byte[] iv = new byte[16];
            System.arraycopy(encryptedData, 0, iv, 0, iv.length);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), ITERATION_COUNT, KEY_LENGTH);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivspec);

            byte[] original = cipher.doFinal(encryptedData, iv.length, encryptedData.length - iv.length);
            return new String(original, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

