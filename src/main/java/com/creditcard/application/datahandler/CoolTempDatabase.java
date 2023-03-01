package com.creditcard.application.datahandler;

import com.creditcard.application.models.cards.CardCreate;
import com.creditcard.application.models.cards.CardResponse;
import com.creditcard.application.models.cards.CreditCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This class behaves as the database. It stores all the CreditCards and allows them to be fetched.
 */
public class CoolTempDatabase {

    public CoolTempDatabase(Map<UUID, CreditCard> cards, ArrayList<String> bannedCountries) {
        this.cards           = cards;
        this.bannedCountries = bannedCountries;
    }

    // Acts as the database which loads the credit cards into memory from the cards.txt
    private final Map<UUID, CreditCard> cards;

    // Contains the list of banned countries.
    private final ArrayList<String> bannedCountries;

    // =================================================================================================================
    // =============================================== Credit Cards ====================================================
    // =================================================================================================================

    /**
     * This takes a valid credit card create and maps it to a CreditCard
     *
     * @param create The body containing the main components for a CreditCard
     * @return The created CreditCard
     */
    public CreditCard insertCreditCard(CardCreate create, CardResponse details) {
        CreditCard creditCard = new CreditCard();
        creditCard.setCardHolder(create.getCardHolder());
        creditCard.setCardNumber(create.getCardNumber());
        creditCard.setDetails(details);
        cards.put(creditCard.getId(), creditCard);
        return creditCard;
    }

    /**
     * Returns all the CreditCards from the Map of CreditCards
     *
     * @return List of CreditCards
     */
    public List<CreditCard> getAllCards() {
        return cards.keySet().stream().sorted().map(cards::get).collect(Collectors.toList());
    }

    /**
     * Fetches a CreditCard by the specific UUID of that card
     *
     * @param id The UUID for the CreditCard we want to retrieve
     * @return The CreditCard with the
     * @throws NullPointerException when there is no CreditCard found
     */
    public CreditCard getCardById(UUID id) throws NullPointerException {
        CreditCard card = cards.get(id);
        if (card != null) {
            return card;
        } else {
            throw new NullPointerException(id.toString());
        }
    }

    /**
     * Checks to see If any of the cards in the map match the value of the card number presented
     *
     * @param number The card number to be searched for
     * @return If a match was found or not
     */
    public boolean isCardDuplicate(String number) {
        return cards.values().stream().anyMatch(c -> c.getCardNumber().equalsIgnoreCase(number));
    }

    // =================================================================================================================
    // ============================================= Banned Countries ==================================================
    // =================================================================================================================

    /**
     * Fetches and returns the list of Countries that are banned
     *
     * @return Banned countries List
     */
    public List<String> getBannedCountries() {
        return bannedCountries;
    }

    /**
     * Checks to see if the provided country is banned.
     *
     * @param countryName The name of the country that might be banned
     * @return If the country is banned or not
     */
    public boolean isBanned(String countryName) {
        return bannedCountries.contains(countryName);
    }

    /**
     * Adds a list of Countries to the banned country list. Does not add the country if it is already banned.
     *
     * @param countries The list of countries to be banned
     * @return The list of banned countries that were not in the already banned list
     */
    public List<String> banCountries(List<String> countries) {
        ArrayList<String> newBan = new ArrayList<>();
        for (String country : countries) {
            if (!isBanned(country)) {
                newBan.add(country);
            }
        }
        bannedCountries.addAll(newBan);
        return newBan;
    }

    /**
     * Removes the list of countries from the banned list
     *
     * @param countries The country names that will be unbanned
     */
    public void unbanCountries(List<String> countries) {
        bannedCountries.removeAll(countries);
    }
}
