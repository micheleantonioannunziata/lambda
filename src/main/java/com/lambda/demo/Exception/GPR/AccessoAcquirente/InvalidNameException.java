package com.lambda.demo.Exception.GPR.AccessoAcquirente;

import com.lambda.demo.Exception.GPR.GPRException;

public class InvalidNameException extends GPRException {

    public InvalidNameException(String message) {
        super(message);
    }
}
