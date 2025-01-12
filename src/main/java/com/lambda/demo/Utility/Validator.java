package com.lambda.demo.Utility;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.regex.Pattern;

public class Validator {


    //regex che la mail deve rispettare
    // L'email deve avere una lunghezza compresa tra 8 e 50 caratteri.
    // Prima della chiocciola:
    //  - Deve esserci almeno un token alfanumerico, che può includere lettere, numeri, e caratteri speciali come punto, percentuale, più e meno.
    // Dopo la chiocciola:
    //  - Deve esserci almeno un token alfanumerico per il dominio, che può includere lettere, numeri, trattini e punti.
    // Dopo il dominio:
    //  - Deve esserci un punto seguito dall'estensione del dominio, composta da 2 a 8 caratteri alfabetici.
    private static final String emailRegex = "^(?=.{8,50}$)[\\w.%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,8}$";

    private static final Pattern emailPattern = Pattern.compile(emailRegex);
    //metodo che confronta l'email con la regex tramite un oggetto Pattern
    public static boolean isValidEmail(String email) {
        if (email == null)
            return false;
        return emailPattern.matcher(email).matches();
    }

    //regex che la password che deve rispettare
    // La password deve avere una lunghezza compresa tra 8 e 50 caratteri.
    // Deve contenere almeno:
    //  - Una lettera minuscola.
    //  - Una lettera maiuscola.
    //  - Un numero.
    //  - Un carattere speciale (non alfanumerico, come simboli o punteggiatura).
    private static final String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\s]).{8,50}$";

    private static final Pattern passwordPattern = Pattern.compile(passwordRegex);
    //metodo che confronta la password con la regex tramite un oggetto Pattern
    public static boolean isValidPassword(String password) {
        if (password == null)
            return false;
        return passwordPattern.matcher(password).matches();
    }

    //da rivedere
    private  static final String partitaIVARegex = "^[\\d+]{11}$";
    private static final Pattern partitaIVAPattern = Pattern.compile(partitaIVARegex);
    public static boolean isValidPartitaIVA(String partitaIVA) {
        if (partitaIVA == null)
            return false;
        return partitaIVAPattern.matcher(partitaIVA).matches();
    }


    // Il nome deve avere una lunghezza di almeno 2 caratteri.
    // Può contenere solo lettere (maiuscole o minuscole) e spazi.
    // Può avere un massimo di 50 caratteri in totale.
    private static final String nomeRegex = "^[A-Za-z]{2,}[A-Za-z ]{0,48}$";
    private static final Pattern nomePattern = Pattern.compile(nomeRegex);
    public static boolean isValidName(String nome){
        if (nome == null)
            return false;
        return nomePattern.matcher(nome).matches();
    }


    // Il cognome deve avere una lunghezza di almeno 2 caratteri.
    // Può contenere solo lettere (maiuscole o minuscole), apostrofi e spazi.
    // La lunghezza totale del cognome può essere al massimo di 50 caratteri.
    private static final String cognomeRegex = "^[A-Za-z]{2,}([A-Za-z' ]{0,48}[A-Za-z])?$";
    private static final Pattern cognomePattern = Pattern.compile(cognomeRegex);
    public static boolean isValidSurname(String cognome){
        if (cognome == null)
            return false;
        return cognomePattern.matcher(cognome).matches();
    }


    private static final String ragioneSocialeRegex = "^[A-Za-z0-9ÀÈÉÌÒÓÙàèéìòóùç&',.\\s-]{2,100}$";
    private static final Pattern ragioneSocialePattern = Pattern.compile(ragioneSocialeRegex);
    public static boolean isValidCompanyName(String ragioneSociale){
        return ragioneSociale != null && ragioneSocialePattern.matcher(ragioneSociale).matches();
    }

    private static final String quantitaRegex = "[1-9][0-9]{0,2}";
    private static final Pattern quantitaPattern = Pattern.compile(quantitaRegex);
    public static boolean isValidQuantita(String quantita){
        if (quantita == null)
            return false;

        return quantitaPattern.matcher(quantita).matches();
    }



    private static final String prezzoRegex = "^[1-9]\\d{0,3}(\\.\\d{1,2})?$";
    private static final Pattern prezzoPattern = Pattern.compile(prezzoRegex);
    public static boolean isValidPrezzo(String prezzo){
        if (prezzo == null)
            return false;

        return prezzoPattern.matcher(prezzo).matches();
    }

    private static final String scontoBaseRegex = "([1-9]?\\d)";
    private static final Pattern scontoBasePattern = Pattern.compile(scontoBaseRegex);
    public static boolean isValidScontoBase(String scontoBase){
        if (scontoBase == null)
            return false;

        return scontoBasePattern.matcher(scontoBase).matches();
    }

    private static final String scontoPremiumRegex = "([1-9]?\\d)";
    private static final Pattern scontoPremiumPattern = Pattern.compile(scontoPremiumRegex);
    public static boolean isValidScontoPremium(String scontoPremium){
        if (scontoPremium == null)
            return false;

        return scontoPremiumPattern.matcher(scontoPremium).matches();
    }


    private static final Set<Integer> ramValues = Set.of(1, 2, 4, 6, 8, 12, 16, 24, 32, 64, 128);
    public static boolean isValidRamValue(String ramValue){
        return ramValue != null && ramValues.contains(Integer.parseInt(ramValue));
    }

    private static final Set<Integer> storageValues = Set.of(4, 6, 8, 12, 16, 24, 32, 64, 128, 256, 512, 1024, 2048);
    public static boolean isValidStorageValue(String storageValue){
        return storageValue != null && storageValues.contains(Integer.parseInt(storageValue));
    }

    private static final Set<String> generalConditionValues = Set.of("Discreta", "Buona", "Ottima", "Eccellente");
    public static boolean isValidGeneralCondition(String generalCondition){
        return generalCondition != null && generalConditionValues.contains(generalCondition);
    }


    private static final String batteriaRegex = "([1-9]?\\d|100)";
    private static final Pattern batteriaPattern = Pattern.compile(batteriaRegex);
    public static boolean isValidBattery(String batteria){
        return batteria != null && batteriaPattern.matcher(batteria).matches();
    }


    private static final String coloreRegex = "^[A-Za-z ]{3,50}$";
    private static final Pattern colorePattern = Pattern.compile(coloreRegex);
    public static boolean isValidColor(String colore){
        return colore != null && colorePattern.matcher(colore).matches();
    }


    private static final String nomeFileRegex = "^[A-Za-z0-9]+([-_ ][A-Za-z0-9]+)*\\.png$";
    private static final Pattern nomeFilePattern = Pattern.compile(nomeFileRegex);
    public static boolean isValidFileName(String nome){
        return nome != null && nomeFilePattern.matcher(nome).matches();
    }

    private static final long MAX_FILE_SIZE = 30L * 1024 * 1024;
    public static boolean isValidFileSize(long fileSize){
        return fileSize <= MAX_FILE_SIZE;
    }


    private static final String destinatarioRegex = "^(?=.{5,50}$)([A-Za-z ]{2,} [A-Za-z]{2,})$";
    private static final Pattern destinatarioPattern = Pattern.compile(destinatarioRegex);
    public static boolean isValidReceiver(String destinatario){
        return destinatario != null && destinatarioPattern.matcher(destinatario).matches();
    }

    private static final String indirizzoRegex = "^(?=.{15,255}$)[A-Za-z' -]+ \\d+, \\d{5} [A-Za-z' -]{3,}$";
    private static final Pattern indirizzoPattern = Pattern.compile(indirizzoRegex);
    public static boolean isValidAddress(String indirizzo) {
        return indirizzo != null && indirizzoPattern.matcher(indirizzo).matches();
    }

    private static final String intestatarioRegex = "^(?=.{5,50}$)([A-Za-z ]{2,} [A-Za-z]{2,})$";
    private static final Pattern intestatarioPattern = Pattern.compile(intestatarioRegex);
    public static boolean isValidCardHolder(String intestatario){
        return intestatario != null && intestatarioPattern.matcher(intestatario).matches();
    }

    private static final String numeroCartaRegex = "\\d{16}";
    private static final Pattern numeroCartaPattern = Pattern.compile(numeroCartaRegex);
    public static boolean isValidCardNumber(String numeroCarta){
        return numeroCarta != null && numeroCartaPattern.matcher(numeroCarta).matches();
    }

    private static final String CVVRegex = "\\d{3}";
    private static final Pattern CVVPattern = Pattern.compile(CVVRegex);
    public static boolean isValidCVV(String cvv){
        return cvv != null && CVVPattern.matcher(cvv).matches();
    }

    private static final String expirationDateRegex = "^(0[1-9]|1[0-2])/(20[2-3][0-9])$";
    private static final Pattern expirationDatePattern = Pattern.compile(expirationDateRegex);
    public static boolean isValidExpirationDate(String expirationDate){
        return expirationDate != null &&
                expirationDatePattern.matcher(expirationDate).matches() &&
                Integer.parseInt(expirationDate.substring(0,2)) >= LocalDateTime.now().getMonthValue() &&
                Integer.parseInt(expirationDate.substring(3)) >= LocalDateTime.now().getYear();
    }
}