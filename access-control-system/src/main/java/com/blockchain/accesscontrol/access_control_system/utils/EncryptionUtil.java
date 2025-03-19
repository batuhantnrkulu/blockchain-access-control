package com.blockchain.accesscontrol.access_control_system.utils;

import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";

    private final AES256TextEncryptor textEncryptor;

    public EncryptionUtil(@Value("${jasypt.encryptor.password}") String secretKey) 
    {
        this.textEncryptor = new AES256TextEncryptor();
        this.textEncryptor.setPassword(secretKey);
    }

    // Encrypt data before storing
    public String encrypt(String data) 
    {
        try {
            return textEncryptor.encrypt(data);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    // Decrypt data when retrieving
    public String decrypt(String encryptedData) 
    {
        try {
            return textEncryptor.decrypt(encryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }
    
    public void encryptFile(InputStream inputStream, OutputStream outputStream, String key) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, getKey(key));
            processFile(cipher, inputStream, outputStream);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting file", e);
        }
    }

    public void decryptFile(InputStream inputStream, OutputStream outputStream, String key) throws Exception {
        try {
            System.out.println("Reached here in decryptFile");
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, getKey(key));
            processFile(cipher, inputStream, outputStream);
        } catch (Exception e) {
            System.out.println("Reaccched here in decryptFile" + e);
            throw new RuntimeException("Error decrypting file", e);
        }
    }

    private static void processFile(Cipher cipher, InputStream inputStream, OutputStream outputStream) throws Exception {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                outputStream.write(output);
            }
        }
        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            outputStream.write(outputBytes);
        }
    }
    
    private static SecretKey getKey(String key) throws Exception {
        byte[] keyBytes = key.getBytes();
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }
}