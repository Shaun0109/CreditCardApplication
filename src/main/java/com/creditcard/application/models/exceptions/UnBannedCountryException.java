package com.creditcard.application.models.exceptions;

/**
 * A class for any Exceptions that are related to an UnbannedCountry
 */
public class UnBannedCountryException extends Exception {
    public UnBannedCountryException() {
        super("An unexpected error has occurred during the attempt to unban the country.");
    }

    public UnBannedCountryException(String msg) {
        super(msg);
    }
}
