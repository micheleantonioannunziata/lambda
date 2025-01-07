package com.lambda.demo.Exception.GA.GestioneOrdini;

import com.lambda.demo.Exception.GA.GAException;

public class InvalidExpirationDateException extends GAException {
    public InvalidExpirationDateException(String message) {
        super(message);
    }
}
