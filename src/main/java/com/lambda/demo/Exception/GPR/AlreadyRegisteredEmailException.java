package com.lambda.demo.Exception.GPR;

public class AlreadyRegisteredEmailException extends GPRException {
    public AlreadyRegisteredEmailException(String message) {
        super(message);
    }
}
