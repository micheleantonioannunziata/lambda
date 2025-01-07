package com.lambda.demo.Exception.GPR.AccessoAcquirente;

import com.lambda.demo.Exception.GPR.GPRException;

public class InvalidPasswordException extends GPRException {

    public InvalidPasswordException(String message) {
        super(message);
    }
}
