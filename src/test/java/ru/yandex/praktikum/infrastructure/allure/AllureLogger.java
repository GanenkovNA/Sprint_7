package ru.yandex.praktikum.infrastructure.allure;

import io.qameta.allure.Allure;
import ru.yandex.praktikum.infrastructure.rest_assured.dto.http.HttpExchangeDto;

public class AllureLogger {

    public static void attachExchange(String name, HttpExchangeDto exchange) {
        Allure.addAttachment(name, exchange.toString());
    }

    public static void attachText(String name, String text) {
        Allure.addAttachment(name, "text/plain", text);
    }

    public static void attachJson(String name, String json) {
        Allure.addAttachment(name, "application/json", json);
    }
}
