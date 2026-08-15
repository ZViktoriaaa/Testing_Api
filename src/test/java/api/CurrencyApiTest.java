package api;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.*;

@Epic("Currency API")
public class CurrencyApiTest {
    private final CurrencyApi currencyApi = new CurrencyApi();

    private void verifyEmptyResponse(Response response) {
        response.then()
                .statusCode(200)
                .body("size()", equalTo(0));
    }

    @DataProvider(name = "currencies")
    public Object[][] currencies() {
        return new Object[][]{
                {"USD", 1},
                {"EUR", 1},
                {"RUB", 100}
        };
    }

    @Description("Проверяет получение курса USD, EUR и RUB, и основные поля ответа API")
    @Test(description = "Проверка курса валюты", dataProvider = "currencies")
    public void checkCurrencyRate(String currency, int expectedScale) {
        Response response = currencyApi.getRate(currency);

        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("amount", matchesPattern("\\d+,\\d+"))
                .body("scale", equalTo(expectedScale))
                .body("grow", notNullValue())
                .body("delta", notNullValue())
                .body("banks", instanceOf(List.class));
    }

    @Description("Проверяет, что запрос неизвестной валюты возвращает пустой ответ")
    @Test(description = "Проверка неизвестной валюты")
    public void checkInvalidCurrency() {
        Response response = currencyApi.getRate("ABC");

        verifyEmptyResponse(response);
    }

    @Description("Проверяет, что пустое значение currency возвращает пустой ответ")
    @Test(description = "Проверка пустой валюты")
    public void checkEmptyCurrency() {
        Response response = currencyApi.getRate("");

        verifyEmptyResponse(response);
    }

    @Description("Проверяет, что запрос без параметра currency возвращает пустой ответ")
    @Test(description = "Проверка запроса без валюты")
    public void checkMissingCurrency() {
        Response response = currencyApi.getRateWithoutCurrency();

        verifyEmptyResponse(response);
    }
}
