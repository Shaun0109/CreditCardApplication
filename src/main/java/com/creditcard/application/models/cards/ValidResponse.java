package com.creditcard.application.models.cards;

/**
 * Once a CreditCard is accepted, this model is used to wrap the data for easy access later
 */
public class ValidResponse {
    public boolean isValid;
    public CardResponse cardDetails;

    public ValidResponse(boolean isValid, CardResponse cardDetails) {
        this.isValid     = isValid;
        this.cardDetails = cardDetails;
    }
}
