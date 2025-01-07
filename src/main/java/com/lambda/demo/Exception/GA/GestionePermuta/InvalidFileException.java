package com.lambda.demo.Exception.GA.GestionePermuta;

import com.lambda.demo.Exception.GA.GAException;

public class InvalidFileException extends GAException {
    public InvalidFileException(String message) {
        super(message);
    }
}
