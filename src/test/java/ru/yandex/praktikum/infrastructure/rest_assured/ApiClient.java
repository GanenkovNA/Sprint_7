package ru.yandex.praktikum.infrastructure.rest_assured;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import ru.yandex.praktikum.infrastructure.allure.AllureLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

import static io.restassured.RestAssured.given;

public class ApiClient {

    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);

    private static final ExchangeCaptureFilter FILTER = new ExchangeCaptureFilter();

    @Step("{actionName}")
    public static Response send(Supplier<Response> requestSupplier, String actionName) {
        try {
            return requestSupplier.get();
        } catch (Exception e) {
            String errorMessage = String.format("❌ Ошибка при выполнении запроса [%s]: %s", actionName, e);
            logger.warn(errorMessage, e);
            AllureLogger.AllureLogger.attachStep(errorMessage);
            throw e;
        }
    }

    public static Response post(String path, Object body, String actionName) {
        return send(() ->
                        given()
                                .filter(FILTER)
                                .contentType(ContentType.JSON)
                                .body(body)
                                .post(path),
                actionName
        );
    }

    public static Response get(String path, String actionName) {
        return send(() ->
                        given()
                                .filter(FILTER)
                                .get(path),
                actionName
        );
    }

    public static Response delete(String path, String actionName) {
        return send(() ->
                        given()
                                .filter(FILTER)
                                .delete(path),
                actionName
        );
    }

    // Можно расширить методами put(), patch(), auth() и др.
}
