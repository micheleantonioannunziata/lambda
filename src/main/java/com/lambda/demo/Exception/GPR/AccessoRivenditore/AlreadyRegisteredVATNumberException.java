package com.lambda.demo.Exception.GPR.AccessoRivenditore;

import com.lambda.demo.Exception.GPR.GPRException;

public class AlreadyRegisteredVATNumberException extends GPRException {
    public AlreadyRegisteredVATNumberException(String message) {
        super(message);
    }
}
