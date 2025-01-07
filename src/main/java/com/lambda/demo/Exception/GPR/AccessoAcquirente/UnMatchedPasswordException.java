package com.lambda.demo.Exception.GPR.AccessoAcquirente;

import com.lambda.demo.Exception.GPR.GPRException;

public class UnMatchedPasswordException extends GPRException {
    public UnMatchedPasswordException(String message) {
        super(message);
    }
}
