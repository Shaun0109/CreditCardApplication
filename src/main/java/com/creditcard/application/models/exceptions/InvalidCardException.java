package com.creditcard.application.models.exceptions;

public class InvalidCardException extends Exception {
    public InvalidCardException(String message) {
        super(message);
    }

    public InvalidCardException() {
        super("Invalid card presented");
    }
}
