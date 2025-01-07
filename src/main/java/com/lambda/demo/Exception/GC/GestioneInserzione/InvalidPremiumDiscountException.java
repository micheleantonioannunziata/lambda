package com.lambda.demo.Exception.GC.GestioneInserzione;

import com.lambda.demo.Exception.GC.GCException;

public class InvalidPremiumDiscountException extends GCException {
    public InvalidPremiumDiscountException(String message) {
        super(message);
    }
}
