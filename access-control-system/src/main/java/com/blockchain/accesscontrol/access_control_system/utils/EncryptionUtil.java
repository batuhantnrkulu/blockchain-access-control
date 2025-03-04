package com.blockchain.accesscontrol.access_control_system.utils;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncryptionUtil {

    private final AES256TextEncryptor textEncryptor;

    public EncryptionUtil(@Value("${jasypt.encryptor.password}") String secretKey) 
    {
        this.textEncryptor = new AES256TextEncryptor();
        this.textEncryptor.setPassword(secretKey);
    }

    // Encrypt data before storing
    public String encrypt(String data) 
    {
        return textEncryptor.encrypt(data);
    }

    // Decrypt data when retrieving
    public String decrypt(String encryptedData) 
    {
        return textEncryptor.decrypt(encryptedData);
    }
}
