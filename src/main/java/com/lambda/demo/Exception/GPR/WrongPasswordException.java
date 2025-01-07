package com.lambda.demo.Exception.GPR;

public class WrongPasswordException extends GPRException {
    public WrongPasswordException(String message) {
        super(message);
    }
}
