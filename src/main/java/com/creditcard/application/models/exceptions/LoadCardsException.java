package com.creditcard.application.models.exceptions;

/**
 * A class for any Exceptions that are related to loading cards from the text file
 */
public class LoadCardsException extends Exception {

    public LoadCardsException(String msg) {
        super(msg);
    }
}
