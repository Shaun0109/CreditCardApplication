package com.creditcard.application.models.exceptions;

/**
 * A class for any Exceptions that are related to a Credit Card being invalid
 */
public class InvalidCardException extends Exception {
    public InvalidCardException(String message) {
        super(message);
    }

    public InvalidCardException() {
        super("Invalid card presented");
    }
}
