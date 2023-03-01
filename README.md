# Intro
We want to write a system that allows our admins to submit a bunch of credit card
numbers for validation.

# Requirements
Build an application that can do the following:
- Allow the user to submit a credit card number.
- Check in which country the card was issued and if that country exists in a list of banned
countries.
- Make the list of banned countries configurable.
- If the card is valid – store it somewhere.
- Don’t capture the same card twice.
- Ability to retrieve all credit card captured
- Ability to retrieve a single credit card
- Provide a Postman Collection that can be used to test the application.
- Provide a comprehensive readme.
- The project needs to include an Open Api 3 Specification yml detailing the endpoints
exposed.
- The project needs to include Unit Tests

# Missing from implementation
- The project needs to include an Open Api 3 Specification yml detailing the endpoints
  exposed.
- The project needs to include Unit Tests

# Additional Features
- CreditCards are saved to a text file and then loaded up into memory whenever the application starts
- The banned country names are saved to a text file and when you unban they are removed
- There is a Custom error response message with a detailed description and error

# What I learnt
- How to setup a [java-spark](https://sparkjava.com/documentation) server
- How to use `java-spark`
- How to use [org.projectlombok](https://projectlombok.org/) to convert my Java classes into a Dataclass
- Using `com.google.code.gson` and `com.fasterxml.jackson` to parse the json
- Credit card numbers are attached to countries
- There is an easter egg response code of 418

---
# Documents
[![Postman](https://img.shields.io/badge/docs-postman-brightgreen.svg)](https://documenter.getpostman.com/view/11181125/2s93CSnq3A)

## Routes
**The default server address is `http://localhost:4567`** 

### Credit Cards
| Function | Route             | Description                                                           |
|----------|-------------------|-----------------------------------------------------------------------|
| POST     | /api/v1/cards/    | Creates the credit card if it passes the criteria in the requirements |
| GET      | /api/v1/cards/    | Fetches the list of credit cards                                      |
| GET      | /api/v1/cards/:id | Fetches a credit card with the provided UUID                          |

### Countries
| Function | Route                    | Description                                                   |
|----------|--------------------------|---------------------------------------------------------------|
| POST     | /api/v1/countries/ban/   | Adds the list of banned country names to the banned list      |
| GET      | /api/v1/countries/ban/   | Fetches the list of banned countries                          |
| POST     | /api/v1/countries/unban/ | Removes the list of banned country names from the banned list |