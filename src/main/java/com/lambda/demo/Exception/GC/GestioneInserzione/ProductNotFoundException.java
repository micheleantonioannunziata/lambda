package com.lambda.demo.Exception.GC.GestioneInserzione;

import com.lambda.demo.Exception.GC.GCException;

public class ProductNotFoundException extends GCException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
