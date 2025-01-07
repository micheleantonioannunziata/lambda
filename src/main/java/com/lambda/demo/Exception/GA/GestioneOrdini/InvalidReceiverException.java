package com.lambda.demo.Exception.GA.GestioneOrdini;

import com.lambda.demo.Exception.GA.GAException;

public class InvalidReceiverException extends GAException {
    public InvalidReceiverException(String message) {
        super(message);
    }
}
