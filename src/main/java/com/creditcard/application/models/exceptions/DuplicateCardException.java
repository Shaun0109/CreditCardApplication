package com.creditcard.application.models.exceptions;

/**
 * A class for any Exceptions that are related to a Credit Card being duplicated
 */
public class DuplicateCardException extends Exception {
    public DuplicateCardException() {
        super("The card already exists.");
    }
}
