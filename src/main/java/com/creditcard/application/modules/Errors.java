package com.creditcard.application.modules;

import com.creditcard.application.models.exceptions.ResponseError;
import com.creditcard.application.models.exceptions.InvalidCardException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;

/**
 * Contains all the basic error messages that can be caught by the ErrorHandler
 * @see ErrorHandler
 */
public abstract class Errors {

    /**
     * Error Response for when there is any unrecognised property in the body of the request
     *
     * @param ex The exception
     * @return The response in an error form
     */
    public ResponseError unrecognisedProperty(UnrecognizedPropertyException ex) {
        return new ResponseError(
                SC_BAD_REQUEST,
                "",
                "UnrecognizedPropertyException: " + ex.getPropertyName()
        );
    }

    /**
     * Error Response for when there is an invalid Credit Card presented
     *
     * @param ex The exception
     * @return The response in an error form
     */
    public ResponseError invalid(InvalidCardException ex) {
        // I use this because it is a fun easter-egg response code
        int HTTP_TEAPOT = 418;
        return new ResponseError(
                HTTP_TEAPOT,
                "The card is an invalid card. No coffee for you!",
                "Invalid credit card: " + ex
        );
    }

    /**
     * Error Response for when there is any json parse failure
     *
     * @param jpe The exception
     * @return The response in an error form
     */
    public ResponseError parseException(JsonParseException jpe) {
        return new ResponseError(
                SC_BAD_REQUEST,
                "There was an error during the attempt to parse the json.",
                "Unable to parse json: " + jpe.getMessage()
        );
    }

    /**
     * The general response message when there is no resource found
     *
     * @param ex The exception
     * @return The response in an error form
     */
    public ResponseError notFound(Exception ex) {
        return new ResponseError(
                SC_NOT_FOUND,
                "Unable to find a resource for the specific requirements.",
                "No resource was found: " + ex.getMessage()
        );
    }

    /**
     * The general response message when there is an unexpected error
     *
     * @param ex The exception
     * @return The response in an error form
     */
    public ResponseError unexpectedError(Exception ex) {
        return new ResponseError(
                SC_BAD_REQUEST,
                "",
                "Oops. An unknown error has occurred, rest assured our devs are hard at work fixing it! Log: " + ex.getMessage()
        );
    }
}
