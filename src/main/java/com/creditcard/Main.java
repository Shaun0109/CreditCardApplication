package com.creditcard;

import com.creditcard.application.datahandler.CoolTempDatabase;
import com.creditcard.application.models.cards.CountryList;
import com.creditcard.application.models.cards.CreditCard;
import com.creditcard.application.models.responses.ResponseError;
import com.creditcard.application.modules.CreditCardModule;
import com.creditcard.application.modules.ErrorHandler;
import com.creditcard.application.modules.FileModule;

import java.util.List;
import java.util.UUID;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static spark.Spark.*;

/**
 * Main class for the application that contains all the routes and instantiated modules
 */
public class Main {

    public static void main(String[] args) {
        // Initialise the error handler
        final ErrorHandler errorHandler = new ErrorHandler();

        try {
            // Initialise core modules
            final FileModule fileModule     = new FileModule();
            final CoolTempDatabase database = new CoolTempDatabase(fileModule);
            final CreditCardModule module   = new CreditCardModule(database);

            // All the routes that can be accessed
            path("/api/v1", () -> {
                path("/cards", () -> {
                    // Insert a credit card if the validation passes
                    post("/", (request, response) -> {
                        response.type("application/json");
                        try {
                            CreditCard creditCard = module.insertCreditCard(request);
                            String json           = database.dataToJson(creditCard);
                            response.status(SC_OK);
                            fileModule.save(json + "~", "cards.txt", true);
                            return json;
                        } catch (Exception ex) {
                            ResponseError error = errorHandler.handleException(ex);
                            response.status(error.getStatusCode());
                            return database.dataToJson(error);
                        }
                    });

                    // Get all the existing credit cards
                    get("/", (request, response) -> {
                        response.type("application/json");
                        try {
                            List<CreditCard> fetchedCards = database.getAllCards();
                            response.status(SC_OK);
                            return database.dataToJson(fetchedCards);
                        } catch (Exception ex) {
                            ResponseError error = errorHandler.handleException(ex);
                            response.status(error.getStatusCode());
                            return database.dataToJson(error);
                        }
                    });

                    // Get the credit card with the specified id
                    get("/:id", (request, response) -> {
                        response.type("application/json");
                        try {
                            CreditCard card = database.getCardById(UUID.fromString(request.params(":id")));
                            response.status(SC_OK);
                            return database.dataToJson(card);
                        } catch (Exception ex) {
                            ResponseError error = errorHandler.handleException(ex);
                            response.status(error.getStatusCode());
                            return database.dataToJson(error);
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
                                String json             = database.dataToJson(countryList);
                                fileModule.saveCountries(countryList.getCountries(), true);
                                return String.format("{\"banned\":%s}", json);
                            } catch (Exception ex) {
                                ResponseError error = errorHandler.handleException(ex);
                                response.status(error.getStatusCode());
                                return database.dataToJson(error);
                            }
                        });

                        // Retrieves the list of banned countries
                        get("/", (request, response) -> {
                            response.type("application/json");
                            return  String.format("{\"banned\":%s}", database.dataToJson(database.getBannedCountries())
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
                            return String.format("{\"unbanned\":%s}", database.dataToJson(unbanned));
                        } catch (Exception ex) {
                            ResponseError error = errorHandler.handleException(ex);
                            response.status(error.getStatusCode());
                            return database.dataToJson(error);
                        }
                    });
                });
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            //TODO: Implement logger
            System.out.println(errorHandler.handleException(ex));
        }
    }
}