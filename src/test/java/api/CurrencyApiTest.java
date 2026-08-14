package api;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("Проверка курса валюты")
    @Description("Проверяет получение курса USD, EUR и RUB, и основные поля ответа API")
    @Test(dataProvider = "currencies")
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

    @DisplayName("Проверка неизвестной валюты")
    @Description("Проверяет, что запрос неизвестной валюты возвращает пустой ответ")
    @Test
    public void checkInvalidCurrency() {
        Response response = currencyApi.getRate("ABC");

        verifyEmptyResponse(response);
    }

    @DisplayName("Проверка пустой валюты")
    @Description("Проверяет, что пустое значение currency возвращает пустой ответ")
    @Test
    public void checkEmptyCurrency() {
        Response response = currencyApi.getRate("");

        verifyEmptyResponse(response);
    }

    @DisplayName("Проверка запроса без валюты")
    @Description("Проверяет, что запрос без параметра currency возвращает пустой ответ")
    @Test
    public void checkMissingCurrency() {
        Response response = currencyApi.getRateWithoutCurrency();

        verifyEmptyResponse(response);
    }
}
