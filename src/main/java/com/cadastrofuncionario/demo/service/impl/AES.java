package com.cadastrofuncionario.demo.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    private SecretKeySpec secretKeySpec;
    private byte[] key;

    public AES(String secret) {
        MessageDigest sha = null;

        try {
            key = secret.getBytes(StandardCharsets.ISO_8859_1);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKeySpec = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    String encrypt(String strToEncryptString) {
        if (strToEncryptString == null) {
            return null; // ou lançar uma exceção customizada
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(strToEncryptString.getBytes(StandardCharsets.ISO_8859_1)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    String decrypt(String strToDecrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
