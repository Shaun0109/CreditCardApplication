package com.creditcard.application.models.exceptions;

/**
 * A class for any Exceptions that are related to an banning a country
 */
public class BannedCountryException extends Exception {
    public BannedCountryException() {
        super("The country is banned");
    }

    public BannedCountryException(String msg) {
        super(msg);
    }
}
