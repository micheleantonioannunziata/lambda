package com.lambda.demo.Exception.GPR.AccessoAcquirente;

import com.lambda.demo.Exception.GPR.GPRException;

public class InvalidEmailException extends GPRException {

    public InvalidEmailException(String message) {
        super(message);
    }
}
