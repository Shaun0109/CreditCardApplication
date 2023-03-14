package com.creditcard.application.modules;

import com.creditcard.application.datahandler.CoolTempDatabase;
import com.creditcard.application.models.cards.CountryList;
import com.creditcard.application.models.cards.CreditCard;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.util.*;

/**
 * Handles all the file related functionality
 */
public class FileModule {

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
            throw new RuntimeException("An error occurred while trying to map the object to JSON.");
        }
    }

    /**
     * Loads the saved CreditCards inside cards.txt
     * @return The list of CreditCards to be loaded into the CoolTempDatabase
     */
    public Map<UUID, CreditCard> loadCards() {
        Map<UUID, CreditCard> cards = new HashMap<>();
        List<String> cardsData      = Arrays.asList(load("cards.txt").split("~"));

        // Remove the empty last line and then load in all the CreditCards from the text file
        for (String data : cardsData.subList(0, cardsData.size() - 1)) {
            try {
                CreditCard card = new ObjectMapper().readValue(data, CreditCard.class);
                cards.put(card.getId(), card);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return cards;
    }

    /**
     * Loads the saved banned counties in banned-countries.txt
     *
     * @return The list of countries to be banned in CoolTempDatabase
     */
    public ArrayList<String> loadBannedCountries() {
        ArrayList<String> countries;
        List<String> data = Arrays.asList(
                load("banned-countries.txt").replaceAll("\n", "").split(",")
        );

        // Remove the empty last line and then load in all the CreditCards from the text file
        if (!data.isEmpty() & !data.get(0).isEmpty()) {
            data.removeIf(String::isEmpty);
            try {
                String create           = String.format("{\"countries\":%s}", dataToJson(data));
                CountryList countryList = new ObjectMapper().readValue(create, CountryList.class);
                countries               = new ArrayList<>(countryList.getCountries());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            countries = new ArrayList<>();
        }
        return countries;
    }

    public String getBannedCountries(CoolTempDatabase database) {
        return String.format("{\"banned\":%s}", dataToJson(database.getBannedCountries()));
    }

    /**
     * Saves the data to the cards.txt file in the resources folder
     * @param data The data to be appended to the file
     */
    public void save(String data, String fileName, boolean append) {
        try (FileWriter fw = new FileWriter("src/main/resources/" + fileName, append);
             PrintWriter out = new PrintWriter(new BufferedWriter(fw))) {
            out.println(data);
        } catch (Exception e) {
            System.out.println("[Error] - Could not save to file " + fileName);
        }
    }

    /**
     * A helper method to save the countries. Allows to be appended or not when a country is (un)banned
     *
     * @param countryList The list of countries being (un)banned
     * @param append      If the content must be appended or not
     */
    public void saveCountries(List<String> countryList, boolean append) {
        if (!countryList.isEmpty() | !append) {
            save(String.join(",", countryList).replace("\n", ""), "banned-countries.txt", append);
        }
    }

    /**
     * Reads the content from the file inside the resource folder.
     *
     * @param filename The name of the file that can be found inside the resource folder
     * @return The content of the file
     */
    public String load(String filename) {
        try (
                FileReader fr = new FileReader("src/main/resources/" + filename);
                BufferedReader br = new BufferedReader(fr)
        ) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(String.format("%s%s", line,"\n"));
            }
            return sb.toString();
        } catch (Exception ex) {
            System.out.println("[ERROR] - Could not load the file " + filename + ". " + ex);
            return "";
        }
    }

}
