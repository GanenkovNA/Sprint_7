package ru.yandex.praktikum.infrastructure.rest_assured;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import ru.yandex.praktikum.infrastructure.allure.AllureLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.function.Supplier;

import static io.restassured.RestAssured.given;
import static io.restassured.config.HttpClientConfig.httpClientConfig;

public class ApiClient {

    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);

    private static final ExchangeCaptureFilter FILTER = new ExchangeCaptureFilter();

    static {
        // Настройка таймаутов (5 сек)
        RestAssured.config = RestAssured.config()
                .httpClient(httpClientConfig()
                        .setParam("http.connection.timeout", 5000)
                        .setParam("http.socket.timeout", 10000)
                        .setParam("http.connection-manager.timeout", 5000)
                );
    }

    @Step("{actionName}")
    public static Response send(Supplier<Response> requestSupplier, String actionName) {
        try {
            return requestSupplier.get();
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof SocketTimeoutException) {
                String message = "⏱ Превышен таймаут ответа от сервера: " + actionName;
                logger.error(message, cause);
                AllureLogger.attachStep(message + "\n" + cause.getMessage());
                throw new RuntimeException(message, cause);
            }

            if (cause instanceof IOException && cause.getMessage().contains("connect timed out")) {
                String message = "⏱ Превышен таймаут подключения к серверу: " + actionName;
                logger.error(message, cause);
                AllureLogger.attachStep(message + "\n" + cause.getMessage());
                throw new RuntimeException(message, cause);
            }

            String errorMessage = String.format("❌ Ошибка при выполнении запроса [%s]: %s", actionName, e);
            logger.warn(errorMessage, e);
            AllureLogger.attachStep(errorMessage);
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
