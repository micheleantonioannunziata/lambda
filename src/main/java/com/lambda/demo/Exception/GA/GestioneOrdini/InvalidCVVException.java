package com.lambda.demo.Exception.GA.GestioneOrdini;

import com.lambda.demo.Exception.GA.GAException;

public class InvalidCVVException extends GAException {
    public InvalidCVVException(String message) {
        super(message);
    }
}
