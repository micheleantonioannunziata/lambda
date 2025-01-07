package com.lambda.demo.Exception.GPR.AccessoRivenditore;

import com.lambda.demo.Exception.GPR.GPRException;

public class InvalidVATNumberException extends GPRException {
    public InvalidVATNumberException(String message) {
        super(message);
    }
}
