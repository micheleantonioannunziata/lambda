package com.lambda.demo.Exception.GPR.AccessoAcquirente;

import com.lambda.demo.Exception.GPR.GPRException;

public class InvalidSurnameException extends GPRException {

    public InvalidSurnameException(String message) {
        super(message);
    }
}
