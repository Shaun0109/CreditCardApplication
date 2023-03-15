package com.creditcard.application.modules;

import com.creditcard.application.datahandler.CoolTempDatabase;
import com.creditcard.application.models.cards.CardCreate;
import com.creditcard.application.models.cards.CardResponse;
import com.creditcard.application.models.cards.CountryList;
import com.creditcard.application.models.cards.CreditCard;
import com.creditcard.application.models.exceptions.BannedCountryException;
import com.creditcard.application.models.exceptions.DuplicateCardException;
import com.creditcard.application.models.exceptions.InvalidCardException;
import com.creditcard.application.models.exceptions.UnBannedCountryException;
import com.creditcard.application.models.responses.ValidResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Has all the functionality for the CreditCards and the Countries
 */
public class CreditCardModule {

    CoolTempDatabase database;

    public CreditCardModule(CoolTempDatabase database) {
        this.database = database;
    }


    /**
     * Makes a request to the provided URL and if there is a OK response it processes the response.
     *
     * @param url The URL that the request will be pinging
     * @return The JSON of the response
     * @throws Exception Any errors that occur that will be caught by the main class.
     */
    public String getJSON(URL url) throws Exception {
        HttpURLConnection response = null;
        try {
            // Set the request properties and make the request to the URL
            response = (HttpURLConnection) url.openConnection();
            response.setRequestMethod("GET");
            response.setRequestProperty("Content-length", "0");
            response.setUseCaches(false);
            response.setAllowUserInteraction(false);
            response.connect();
            int status = response.getResponseCode();

            // If there is an OK status then process the response
            switch (status) {
                case 200, 201 -> {
                    BufferedReader br = new BufferedReader(new InputStreamReader(response.getInputStream()));
                    StringBuilder sb  = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();
                    return sb.toString();
                }
            }
        } finally {
            if (response != null) {
                response.disconnect();
            }
        }
        return null;
    }

    // =================================================================================================================
    // =============================================== Credit Cards ====================================================
    // =================================================================================================================

    /**
     * Inserts the card into the list of cards in memory.
     *
     * @param request The request that contains the body for the CreditCard
     * @return The created CreditCard
     * @throws Exception Any errors that occur that will be caught by the main class
     * @implNote Usually this would insert into the DB. But for this case it is passed back to main and then written to
     * the cards.txt file
     */
    public CreditCard insertCreditCard(Request request) throws Exception {
        CardCreate creation     = new ObjectMapper().readValue(request.body(), CardCreate.class);
        ValidResponse validCard = validateCard(creation);

        if (!validCard.isValid) {
            throw new InvalidCardException();
        }

        return database.insertCreditCard(creation, validCard.cardDetails);
    }

    /**
     * This function is responsible for ensuring that the credit card submitted is a valid card based on the
     * provided criteria.
     *
     * @param create The body of the create CreditCard
     * @return If the create body is valid or not
     * @throws Exception Any errors that occur that will be caught by the main class.
     */
    private ValidResponse validateCard(CardCreate create) throws Exception {

        String cardNumber = create.getCardNumber().replace(" ", "");
        String digits     = cardNumber.substring(0, 9);

        URL url     = new URL("https://lookup.binlist.net/" + digits);
        String json = getJSON(url);

        // Map the response to the card and get the validation criteria
        CardResponse cardDetails = new ObjectMapper().readValue(json, CardResponse.class);
        boolean isBanned         = database.isBanned(cardDetails.getCountry().getName());
        boolean isDuplicate      = database.isCardDuplicate(cardNumber);
        if (isDuplicate) {
            throw new DuplicateCardException();
        } else {
            if (cardNumber.length() == 16) {
                if (!isBanned) {
                    return new ValidResponse(true, cardDetails);
                } else {
                    throw new BannedCountryException();
                }
            } else {
                throw new InvalidCardException("The card number provided is invalid.");
            }
        }
    }

    // =================================================================================================================
    // ============================================= Banned Countries ==================================================
    // =================================================================================================================

    /**
     * Adds a country to a banned list
     *
     * @param request The request that contains the body for the banned countries
     * @return The list of countries to be banned
     * @throws BannedCountryException Any errors that occur that will be caught by the main class
     */
    public CountryList banCountry(Request request) throws BannedCountryException {
        try {
            CountryList countryList = new ObjectMapper().readValue(request.body(), CountryList.class);
            List<String> countries  = database.banCountries(countryList.getCountries());
            countryList.setCountries(countries);
            return countryList;
        } catch (IOException ex) {
            throw new BannedCountryException(ex.getMessage());
        } catch (Exception ex) {
            throw new BannedCountryException("An unexpected error occurred while trying to ban a country.");
        }
    }

    /**
     * Removes a country to a banned list
     *
     * @param request The request that contains the body for the unbanned countries
     * @return The list of countries to be unbanned
     * @throws UnBannedCountryException Any errors that occur that will be caught by the main class
     */
    public CountryList unbanCountry(Request request) throws UnBannedCountryException {
        try {
            CountryList countryList = new ObjectMapper().readValue(request.body(), CountryList.class);
            database.unbanCountries(countryList.getCountries());
            return countryList;
        } catch (IOException ex) {
            throw new UnBannedCountryException(ex.getMessage());
        } catch (Exception ex) {
            throw new UnBannedCountryException();
        }
    }

}
