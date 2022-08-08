package com.example.demo;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

public class JasyptConfigTest {


    @Test
    void jasypt() {
        String email = "tkddlfdlfemd@gmail.com";
        String smtpPassword = "pputllfxgpzbmunw";

        String encryptEmail = jasyptEncrypt(email);
        String encryptSmtpPassword = jasyptEncrypt(smtpPassword);

        System.out.println("encryptUsername : " + encryptEmail);
        System.out.println("encryptPassword : " + encryptSmtpPassword);

    }

    private String jasyptEncrypt(String input) {
        String key = ""; // Key
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);
        return encryptor.encrypt(input);
    }

    private String jasyptDecryt(String input) {
        String key = ""; // Key
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);
        return encryptor.decrypt(input);
    }
}
