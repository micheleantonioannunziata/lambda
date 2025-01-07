package com.lambda.demo.Utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encrypt {
    /*Cripta una stringa
    @param string La stringa da criptare.
    @return Una rappresentazione esadecimale dell'hash della stringa.
            @throws NoSuchAlgorithmException Se l'algoritmo di hashing non è disponibile.*/
    public static String encrypt(String password) {
        try {
            // Creazione dell'istanza di MessageDigest con algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Esegui l'hash della password e ottieni l'array di byte
            byte[] hashBytes = digest.digest(password.getBytes());

            // Converte l'array di byte in una rappresentazione esadecimale
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }

            // Restituisce la stringa hashata
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Gestisce eventuali errori nell'algoritmo di hash
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}