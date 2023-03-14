package com.creditcard.application.models.exceptions;

public class InvalidCardException extends Exception {
    public InvalidCardException() {
        super("An invalid card was presented.");
    }
}
