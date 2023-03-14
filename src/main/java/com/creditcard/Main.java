package com.creditcard;

import com.creditcard.application.datahandler.CoolTempDatabase;
import com.creditcard.application.models.exceptions.ResponseError;
import com.creditcard.application.models.cards.CountryList;
import com.creditcard.application.models.cards.CreditCard;
import com.creditcard.application.modules.CreditCardModule;
import com.creditcard.application.modules.ErrorHandler;
import com.creditcard.application.modules.FileModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.UUID;

import static javax.servlet.http.HttpServletResponse.*;
import static spark.Spark.*;

/**
 * Main class for the application that contains all the routes and instantiated modules
 */
public class Main {

    /**
     * Converts the Object/Model to json, so it can be used in a response
     *
     * @param data The Object/Model that will be mapped to json
     * @return The Object/Model in json form
     */
    private static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while trying to map the object to JSON.");
        }
    }

    public static void main(String[] args) {

        // Initialise core modules
        final FileModule fileModule     = new FileModule();
        final CoolTempDatabase database = new CoolTempDatabase(fileModule.loadCards(), fileModule.loadBannedCountries());
        final CreditCardModule module   = new CreditCardModule(database);
        final ErrorHandler errorHandler = new ErrorHandler();

        // All the routes that can be accessed
        path("/api/v1", () -> {
            path("/cards", () -> {
                // Insert a credit card if the validation passes
                post("/", (request, response) -> {
                    response.type("application/json");
                    try {
                        CreditCard creditCard = module.insertCreditCard(request);
                        String json           = dataToJson(creditCard);
                        response.status(SC_OK);
                        fileModule.save(json + "~", "cards.txt", true);
                        return json;
                    } catch (Exception ex) {
                        ResponseError error = errorHandler.handleException(ex);
                        response.status(error.getStatusCode());
                        return dataToJson(error);
                    }
                });

                // Get all the existing credit cards
                get("/", (request, response) -> {
                    response.type("application/json");
                    try {
                        List<CreditCard> fetchedCards = database.getAllCards();
                        response.status(SC_OK);
                        response.type("application/json");
                        return dataToJson(fetchedCards);
                    } catch (Exception ex) {
                        ResponseError error = errorHandler.handleException(ex);
                        response.status(error.getStatusCode());
                        return dataToJson(error);
                    }
                });

                // Get the credit card with the specified id
                get("/:id", (request, response) -> {
                    response.type("application/json");
                    try {
                        CreditCard card = database.getCardById(UUID.fromString(request.params(":id")));
                        response.status(SC_OK);
                        return dataToJson(card);
                    } catch (Exception ex) {
                        ResponseError error = errorHandler.handleException(ex);
                        response.status(error.getStatusCode());
                        return dataToJson(error);
                    }
                });
            });
            path("/countries", () -> {
                path("/ban", () -> {
                    // Bans a new county or list of countries
                    post("/", (request, response) -> {
                        response.type("application/json");
                        response.status(SC_BAD_REQUEST);
                        try {
                            response.status(SC_OK);
                            CountryList countryList = module.banCountry(request);
                            String json             = dataToJson(countryList);
                            fileModule.saveCountries(countryList.getCountries(), true);
                            return String.format("{\"banned\":%s}", json);
                        } catch (Exception ex) { //TODO: Specific ban country error handling
                            ResponseError error = errorHandler.handleException(ex);
                            response.status(error.getStatusCode());
                            return dataToJson(error);
                        }
                    });

                    // Retrieves the list of banned countries
                    get("/", (request, response) -> {
                        response.type("application/json");
                        return  String.format("{\"banned\":%s}", dataToJson(database.getBannedCountries())
                        );
                    });
                });
                // Unbans a country
                post("/unban", (request, response) -> {
                    response.type("application/json");
                    response.status(SC_BAD_REQUEST);
                    try {
                        response.status(SC_OK);
                        CountryList unbanned = module.unbanCountry(request);
                        fileModule.saveCountries(database.getBannedCountries(), false);
                        return String.format("{\"unbanned\":%s}", dataToJson(unbanned));
                    } catch (Exception ex) { //TODO: Specific unban country error handling
                        ResponseError error = errorHandler.handleException(ex);
                        response.status(error.getStatusCode());
                        return dataToJson(error);
                    }
                });
            });
        });
    }
}