package com.lambda.demo.Exception.GA.GestioneOrdini;

import com.lambda.demo.Exception.GA.GAException;

public class InvalidCardHolderException extends GAException {
    public InvalidCardHolderException(String message) {
        super(message);
    }
}
