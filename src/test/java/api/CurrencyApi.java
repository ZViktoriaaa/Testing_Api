package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import utils.PropertyReader;

import static io.restassured.RestAssured.given;

public class CurrencyApi {
    private static final String BASE_URL = PropertyReader.getProperty("base.url");
    private static final String RATE_TYPE = PropertyReader.getProperty("rate.type");

    @Step("Получить курс валюты: {currency}")
    public Response getRate(String currency) {

        return given()
                .queryParam("currency", currency)
                .queryParam("type", RATE_TYPE)
                .when()
                .get(BASE_URL);
    }

    @Step("Получить курс без параметра currency")
    public Response getRateWithoutCurrency() {

        return given()
                .queryParam("type", RATE_TYPE)
                .when()
                .get(BASE_URL);
    }
}
