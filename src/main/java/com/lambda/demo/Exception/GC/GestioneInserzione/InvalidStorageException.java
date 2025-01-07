package com.lambda.demo.Exception.GC.GestioneInserzione;

import com.lambda.demo.Exception.GC.GCException;

public class InvalidStorageException extends GCException {
    public InvalidStorageException(String message) {
        super(message);
    }
}
