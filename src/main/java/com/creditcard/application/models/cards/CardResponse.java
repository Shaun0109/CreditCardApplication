package com.creditcard.application.models.cards;

import lombok.Data;

import java.util.Map;

/**
 * Represents the response from the API call to fetch the card details
 *
 * @apiNote This will no longer work from the 15th of March 2023
 */

@Data
public class CardResponse {
    Map<String, Object> number;
    String scheme;
    String type;
    String brand;
    boolean prepaid;
    Country country;
    Map<String, String> bank;
}
