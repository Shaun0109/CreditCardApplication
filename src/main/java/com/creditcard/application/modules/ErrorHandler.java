package com.creditcard.application.modules;

import com.creditcard.application.models.exceptions.ResponseError;
import com.creditcard.application.models.exceptions.InvalidCardException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

/**
 * Responsible for handling any of the errors that occur during attempts of different application processes
 */
public class ErrorHandler extends Errors {

    public ResponseError handleException(Exception ex) {
        // if (ex instanceof JsonParseException jpe) {
        //     return parseException(jpe);
        // } else if (ex instanceof UnrecognizedPropertyException upe) {
        //     return unrecognisedProperty(upe);
        // } else if (ex instanceof InvalidCardException ice) {
        //     return invalid(ice);
        // } else if (ex instanceof NullPointerException npe) {
        //     return notFound(npe);
        // } else {
        //     return unexpectedError(ex);
        // }
        return unexpectedError(ex);
    }

}
