package com.creditcard.application.models.cards;

import lombok.Data;

/**
 * Represents a Country for the response from the API call to fetch the card details
 *
 * @apiNote This will no longer work from the 15th March 2023
 */
@Data
public class Country {
    String numeric;
    String alpha2;
    String name;
    String emoji;
    String currency;
    int latitude;
    int longitude;
}
