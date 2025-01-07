package com.lambda.demo.Exception.GA.GestioneOrdini;

import com.lambda.demo.Exception.GA.GAException;

public class InvalidCardNumberException extends GAException {
    public InvalidCardNumberException(String message) {
        super(message);
    }
}
