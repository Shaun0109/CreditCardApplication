package com.creditcard.application.models.exceptions;

import lombok.Data;

/**
 * This represents an error response that contains the relevant information for the error
 */
@Data
public class ResponseError {
    int StatusCode;
    String description;
    String error;

    public ResponseError(int statusCode, String description, String error) {
        String defaultDescription = "An error occurred while trying to do something awesome for you!";

        this.StatusCode  = statusCode;
        this.description = description.equalsIgnoreCase("") ? defaultDescription : description;
        this.error       = error;
    }
}
