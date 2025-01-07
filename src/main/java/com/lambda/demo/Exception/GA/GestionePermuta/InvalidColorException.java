package com.lambda.demo.Exception.GA.GestionePermuta;

import com.lambda.demo.Exception.GA.GAException;

public class InvalidColorException extends GAException {
    public InvalidColorException(String message) {
        super(message);
    }
}
