package com.creditcard.application.models.cards;

import lombok.Data;

import java.util.List;

/**
 * Acts as a response for a List of Countries
 */
@Data
public class CountryList {
    List<String> countries;
}
