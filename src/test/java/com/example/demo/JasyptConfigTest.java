package com.example.demo;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

public class JasyptConfigTest {


    @Test
    public void jasypt() {

        System.out.println(jasyptEncrypt("tkddlfdlfemd@gmail.com"));

    }

    private String jasyptEncrypt(String input) {
        String key = "1234"; // Key
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);
        return encryptor.encrypt(input);
    }

    private String jasyptDecryt(String input) {
        String key = "1234"; // Key
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);
        return encryptor.decrypt(input);
    }
}
