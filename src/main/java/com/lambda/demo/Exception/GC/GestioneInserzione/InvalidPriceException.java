package com.lambda.demo.Exception.GC.GestioneInserzione;

import com.lambda.demo.Exception.GC.GCException;

public class InvalidPriceException extends GCException {

    public InvalidPriceException(String message) {
        super(message);
    }
}
