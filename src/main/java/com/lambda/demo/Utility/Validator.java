package com.lambda.demo.Utility;

import java.util.regex.Pattern;

public class Validator {
    //regex che la mail deve rispettare
    private static final String emailRegex = "^[\\w.%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,8}$";
    private static final Pattern emailPattern = Pattern.compile(emailRegex);
    //metodo che confronta l'email con la regex tramite un oggetto Pattern
    public static boolean isValidEmail(String email) {
        if (email == null)
            return false;
        return emailPattern.matcher(email).matches();
    }

    //regex che la password che deve rispettare
    private static final String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,50}$";

    private static final Pattern passwordPattern = Pattern.compile(passwordRegex);
    //metodo che confronta la password con la regex tramite un oggetto Pattern
    public static boolean isValidPassword(String password) {
        if (password == null)
            return false;
        return passwordPattern.matcher(password).matches();
    }

    private  static final String partitaIVARegex = "^[\\d+]{11}$";
    private static final Pattern partitaIVAPattern = Pattern.compile(partitaIVARegex);
    public static boolean isValidPartitaIVA(String partitaIVA) {
        if (partitaIVA == null)
            return false;
        return partitaIVAPattern.matcher(partitaIVA).matches();
    }

    private static final String nomeRegex = "^[A-Za-z]+$";
    private static final Pattern nomePattern = Pattern.compile(nomeRegex);
    public static boolean isValidName(String nome){
        if (nome == null)
            return false;
        return nomePattern.matcher(nome).matches();
    }

    private static final String cognomeRegex = "^[A-Za-z]+(?:\'[A-Za-z]+)*$";
    private static final Pattern cognomePattern = Pattern.compile(cognomeRegex);
    public static boolean isValidSurname(String cognome){
        if (cognome == null)
            return false;
        return cognomePattern.matcher(cognome).matches();
    }

    private static final String indirizzoRegex = "^[A-Za-z' -]+ \\d+, \\d{5} [A-Za-z' -]+$";
    private static final Pattern indirizzoPattern = Pattern.compile(indirizzoRegex);
    public static boolean isValidAddress(String indirizzo){
        if (indirizzo == null)
            return false;
        return indirizzoPattern.matcher(indirizzo).matches();
    }
}
