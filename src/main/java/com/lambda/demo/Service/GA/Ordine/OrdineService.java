package com.lambda.demo.Service.GA.Ordine;

import com.lambda.demo.Exception.GA.GAException;

public interface OrdineService {

    void cardCheckoutValidation(String destinatario, String indirizzo, String intestatario, String numeroCarta, String cvv, String scadenza) throws GAException;

    void lambdaCheckoutValidation(String destinatario, String indirizzo) throws GAException;

    void checkoutFinalization(String destinatario, String indirizzo, String intestatario, String numeroCarta, String cvv, String scadenza) throws GAException;

}
