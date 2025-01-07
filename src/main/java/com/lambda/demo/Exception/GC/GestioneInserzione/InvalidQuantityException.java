package com.lambda.demo.Exception.GC.GestioneInserzione;

import com.lambda.demo.Exception.GC.GCException;

public class InvalidQuantityException extends GCException {

    public InvalidQuantityException(String message) {
        super(message);
    }
}
