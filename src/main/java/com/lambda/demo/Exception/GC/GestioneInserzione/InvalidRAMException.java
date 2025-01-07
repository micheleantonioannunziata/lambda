package com.lambda.demo.Exception.GC.GestioneInserzione;

import com.lambda.demo.Exception.GC.GCException;

public class InvalidRAMException extends GCException {

    public InvalidRAMException(String message) {
        super(message);
    }
}
