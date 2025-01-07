package com.lambda.demo.Service.GA.Ordine;

import com.lambda.demo.Exception.GA.GAException;
import jakarta.servlet.http.HttpServletRequest;

public interface OrdineService {

    void checkout(String destinatario, String indirizzo, String intestatario, String numeroCarta, String cvv, String scadenza) throws GAException;

    void checkoutFinalization(boolean lambda, String destinatario, String indirizzo, String numeroCarta, HttpServletRequest req);
}
