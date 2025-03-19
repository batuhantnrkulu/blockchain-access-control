package com.blockchain.accesscontrol.access_control_system.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    private final AES256TextEncryptor textEncryptor = new AES256TextEncryptor();

    @Value("${jasypt.encryptor.password}") 
    private String secretKey;
    
    @Value("${aes.secret.key}")
    private String aesSecretKey;

    private static final byte[] IV = new byte[16]; // Static IV for simplicity
    
    @PostConstruct
    public void init() {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("EncryptionUtil: jasypt.encryptor.password is not set!");
        }
        
        if (aesSecretKey == null || aesSecretKey.isEmpty()) {
            throw new IllegalStateException("aes.secret.key is not set!");
        }
        
        textEncryptor.setPassword(secretKey);
    }

    // Encrypt data before storing
    public String encrypt(String data) 
    {
    	System.out.println("Loaded Secret Key: " + secretKey);
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
    
    public void encryptFile(InputStream inputStream, OutputStream outputStream) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKey key = getKey(aesSecretKey);
            IvParameterSpec ivSpec = new IvParameterSpec(IV); // Use a random IV in production
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec); // Include IV
            processFile(cipher, inputStream, outputStream);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting file", e);
        }
    }

    public void decryptFile(InputStream inputStream, OutputStream outputStream) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKey key = getKey(aesSecretKey);
            IvParameterSpec ivSpec = new IvParameterSpec(IV); // Use the same IV for decryption
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec); // Include IV
            processFile(cipher, inputStream, outputStream);
        } catch (Exception e) {
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
	  // Trim whitespace and decode
	  byte[] keyBytes = Base64.getDecoder().decode(key.trim());
	  
	  // Validate key length for AES-256
	  if (keyBytes.length != 32) {
	    throw new IllegalArgumentException(
	      "Invalid AES key length: " + keyBytes.length + " bytes (must be 32 bytes)"
	    );
	  }
	  
	  return new SecretKeySpec(keyBytes, ALGORITHM);
	}
}