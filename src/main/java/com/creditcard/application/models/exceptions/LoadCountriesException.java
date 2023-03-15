package com.creditcard.application.models.exceptions;

/**
 * A class for any Exceptions that are related to an banning a country
 */
public class LoadCountriesException extends Exception {

    public LoadCountriesException(String msg) {
        super(msg);
    }
}
