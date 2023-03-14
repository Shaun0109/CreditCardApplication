package com.creditcard.application.modules;

import com.creditcard.application.models.ResponseError;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

public class ErrorHandler implements Errors {

    public ResponseError handleError(Exception ex) {
        if (ex instanceof UnrecognizedPropertyException upe) {

        }
    }

}
