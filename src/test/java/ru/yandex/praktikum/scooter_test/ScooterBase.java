package ru.yandex.praktikum.scooter_test;

import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.junit.Before;
import org.slf4j.LoggerFactory;
import ru.yandex.praktikum.infrastructure.allure.AllureLogger;
import ru.yandex.praktikum.infrastructure.rest_assured.ExchangeCaptureFilter;
import ru.yandex.praktikum.infrastructure.rest_assured.dto.http.HttpExchangeDto;

public class ScooterBase {

    private static final Logger logger = LoggerFactory.getLogger(ScooterBase.class);

    protected static final int DEFAULT_LENGTH_OF_GENERATED_STRING = 10;

    @Rule
    public TestName testName = new TestName();

    protected String getCurrentTestMethod() {
        return testName.getMethodName();
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    protected void methodBeforeWithLog(Runnable action){
        logger.info("{} - подготовка к тесту...", getCurrentTestMethod());

        action.run();

        logger.info("{} - подготовка к тесту завершена", getCurrentTestMethod());
    }

    protected void methodTestWithLog(Runnable action){
        logger.info("{} - тест выполняется...", getCurrentTestMethod());

        action.run();

        logger.info("{} - тест пройден", getCurrentTestMethod());
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

    protected void safeCleanUp(Runnable action){
        try {
            logger.info("{} - очистка после теста...", getCurrentTestMethod());

            action.run();

            logger.info("{} - очистка после теста завершена", getCurrentTestMethod());
        } catch (Throwable e) {
            logger.warn("{} - очистка после теста завершилась с ошибкой: \n{}", getCurrentTestMethod(),e.getMessage());
            Allure.step("Очистка после теста завершилась с ошибкой (НЕ влияет на результат теста): \n" + e.getMessage());
            // не пробрасываем исключение, чтобы сам тест не оказался проваленным
        }
    }
}
