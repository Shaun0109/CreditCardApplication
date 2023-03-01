package com.creditcard.application.models.cards;

import lombok.Data;

/**
 * CardCreate represents the body that the request must contain in order to create a CreditCard
 */
@Data
public class CardCreate {
    private String cardNumber;
    private String cardHolder;
}
