package com.bank.exception;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(String message, double deficit) {
        super(message + " Deficit: " + deficit);
    }
}
