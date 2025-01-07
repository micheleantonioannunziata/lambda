package com.lambda.demo.Exception.GA.GestioneOrdini;

import com.lambda.demo.Exception.GA.GAException;

public class InvalidAddressException extends GAException {
    public InvalidAddressException(String message) {
        super(message);
    }
}
