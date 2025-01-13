package com.lambda.demo.Utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypt {
    /**
     * gestisce la logica di encrypting della password
     *
     * @param password stringa che rappresenta la password
     * @see String
     */
    public static String encrypt(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}