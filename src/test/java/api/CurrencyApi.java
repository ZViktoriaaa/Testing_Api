package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import utils.PropertyReader;

import static io.restassured.RestAssured.given;

public class CurrencyApi {
    private static final String BASE_URL = PropertyReader.getProperty("base.url");
    private static final String RATE_ENDPOINT = PropertyReader.getProperty("rate.endpoint");

    @Step("Получить курс валюты: {currency}")
    public Response getRate(String currency) {

        return given()
                .queryParam("currency", currency)
                .when()
                .get(BASE_URL + RATE_ENDPOINT);
    }

    @Step("Получить курс без параметра currency")
    public Response getRateWithoutCurrency() {

        return given()
                .when()
                .get(BASE_URL + RATE_ENDPOINT);
    }
}
