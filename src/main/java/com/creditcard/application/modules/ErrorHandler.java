package com.creditcard.application.modules;

import com.creditcard.application.models.exceptions.BannedCountryException;
import com.creditcard.application.models.exceptions.DuplicateCardException;
import com.creditcard.application.models.exceptions.InvalidCardException;
import com.creditcard.application.models.exceptions.UnBannedCountryException;
import com.creditcard.application.models.responses.ResponseError;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

/**
 * Responsible for handling any of the errors that occur during attempts of different application processes
 */
public class ErrorHandler extends Errors {

    /**
     * Gets an error and pattern matches it to a specific type and then provides the appropriate error.
     *
     * @param ex The exception that has to be handled
     * @return The appropriate ResponseError for the provided exception.
     * @see ResponseError
     */
    public ResponseError handleException(Exception ex) {
        return switch (ex) {
            case JsonParseException jpe            -> parseException(jpe);
            case UnrecognizedPropertyException upe -> unrecognisedProperty(upe);
            case BannedCountryException bc         -> bannedCountry(bc);
            case UnBannedCountryException ubc      -> unBannedCountry(ubc);
            case InvalidCardException ice          -> invalid(ice);
            case DuplicateCardException dce        -> duplicateCard(dce);
            case NullPointerException npe          -> notFound(npe);
            default                                -> unexpectedError(ex);
        };
    }

}
