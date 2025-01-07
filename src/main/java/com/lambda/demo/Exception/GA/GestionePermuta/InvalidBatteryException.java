package com.lambda.demo.Exception.GA.GestionePermuta;

import com.lambda.demo.Exception.GA.GAException;

public class InvalidBatteryException extends GAException {
    public InvalidBatteryException(String message) {
        super(message);
    }
}
