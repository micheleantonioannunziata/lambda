package com.lambda.demo.Exception.GPR.AccessoRivenditore;

import com.lambda.demo.Exception.GPR.GPRException;

public class InvalidCompanyNameException extends GPRException {
    public InvalidCompanyNameException(String message) {
        super(message);
    }
}
