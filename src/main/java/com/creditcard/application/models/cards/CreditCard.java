package com.creditcard.application.models.cards;

import lombok.Data;

import java.util.UUID;

/**
 * The Model that represents a CreditCard
 */
@Data
public class CreditCard {
    private String cardNumber;
    private String cardHolder;
    private UUID id = UUID.randomUUID();
    // This is the data from the 3rd party api.
    private CardResponse details;
}
