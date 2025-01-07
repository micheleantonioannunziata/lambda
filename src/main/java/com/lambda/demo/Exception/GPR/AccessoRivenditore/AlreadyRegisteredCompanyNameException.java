package com.lambda.demo.Exception.GPR.AccessoRivenditore;

import com.lambda.demo.Exception.GPR.GPRException;

public class AlreadyRegisteredCompanyNameException extends GPRException {
    public AlreadyRegisteredCompanyNameException(String message) {
        super(message);
    }
}
