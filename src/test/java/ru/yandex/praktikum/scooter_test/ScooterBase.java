package ru.yandex.praktikum.scooter_test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.junit.Before;
import org.slf4j.LoggerFactory;
import ru.yandex.praktikum.infrastructure.allure.AllureLogger;
import ru.yandex.praktikum.infrastructure.rest_assured.ExchangeCaptureFilter;
import ru.yandex.praktikum.infrastructure.rest_assured.dto.http.HttpExchangeDto;

public class ScooterBase {

    private static final Logger logger = LoggerFactory.getLogger(ScooterBase.class);

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    protected void assertStatusCode(Response response, int expectedStatusCode, String methodName) {
        try {
            response.then().statusCode(expectedStatusCode);
        } catch (AssertionError e) {
            logger.error("Ошибка статус-кода! Ожидался `{}`, получен: `{}`!", expectedStatusCode, response.getStatusCode());
            logResponseAndRequest(methodName);
            throw e;
        }
    }

    protected void assertBody(Response response, String jsonPath, Matcher<?> matcher, String description, String methodName) {
        try {
            response.then().assertThat().body(jsonPath, matcher);
        } catch (AssertionError e) {
            logger.error("Ошибка проверки тела ответа: {}", description);
            logResponseAndRequest(methodName);
            throw e;
        }
    }

    private void logResponseAndRequest(String methodName) {
        HttpExchangeDto exchange = ExchangeCaptureFilter.getLastExchange();
        logger.debug("Последний HTTP обмен:\n{}", exchange);
        AllureLogger.attachExchange("HTTP обмен (" + methodName + ")", exchange);
    }
}
