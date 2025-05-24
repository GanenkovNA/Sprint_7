package ru.yandex.praktikum.scooter.courier;

import ru.yandex.praktikum.courier.CourierEntity;
import ru.yandex.praktikum.courier.dto.CourierCreationRequestDto;
import ru.yandex.praktikum.courier.dto.CourierLoginRequestDto;
import ru.yandex.praktikum.courier.dto.CourierLoginResponseDto;

import ru.yandex.praktikum.log_services.allure.AllureLogger;
import ru.yandex.praktikum.log_services.rest_assured.httpdto.HttpExchangeDto;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.praktikum.log_services.rest_assured.ExchangeCaptureFilter;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;


public class CourierBase {
    private static final Logger logger = LoggerFactory.getLogger(CourierBase.class);

    protected CourierEntity courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    protected void assertStatusCode(Response response, int expectedStatusCode) {
        try {
            response.then().statusCode(expectedStatusCode);
        } catch (AssertionError e) {
            logger.error("Ошибка статус-кода! Ожидался `{}`, получен: `{}`!", expectedStatusCode, response.getStatusCode());
            logResponseAndRequest();
            throw e;
        }
    }

    protected void assertBody(Response response, String jsonPath, Matcher<?> matcher, String description) {
        try {
            response.then().assertThat().body(jsonPath, matcher);
        } catch (AssertionError e) {
            logger.error("Ошибка проверки тела ответа: {}", description);
            logResponseAndRequest();
            throw e;
        }
    }

    private void logResponseAndRequest() {
        HttpExchangeDto exchange = ExchangeCaptureFilter.getLastExchange();
        logger.debug("Последний HTTP обмен:\n{}", exchange);
        AllureLogger.attachExchange("HTTP обмен (Create Courier)", exchange);
    }


    @Step("Создание сущности курьера с валидными данными")
    public void createValidCourierEntity(){
        courier = CourierEntity.builder()
                .login("bobr")
                .password("null")
                .firstName("superman")
                .build();

        logger.debug("Создана сущность курьера: {}", courier);
    }

    //@Step("Создание курьера")
    public Response addNewCourier() {
        CourierCreationRequestDto request = CourierCreationRequestDto.builder()
                .login(courier.getLogin())
                .password(courier.getPassword())
                .firstName(courier.getFirstName())
                .build();

        Response response = given()
                .filter(new ExchangeCaptureFilter())
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/v1/courier");

        logger.debug("Отправлен запрос на создание курьера {}", courier.getLogin());
        return response;
    }

    @Step("Проверка создания клиента (кода статуса `201` и значения `ok`)")
    public void addNewCourierAndCheck(){
        Response response = addNewCourier();

        assertStatusCode(response, 201);

        assertBody(response, "ok", equalTo(true), "Ожидался `ok = true`");

        courier.setCreated(true);
        logger.debug("Успешное создание курьера {}", courier.getLogin());
    }

    @Step("Логин курьера")
    public Response loginCourier(){
        CourierLoginRequestDto request = CourierLoginRequestDto.builder()
                .login(courier.getLogin())
                .password(courier.getPassword())
                .build();
        if (courier.isCreated()) {
            Response response = given()
                    .log().ifValidationFails()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .post("/api/v1/courier/login");

            logger.debug("Отправлен запрос на логин курьера {}", courier.getLogin());
            return response;
        }
        else {
            logger.debug("Запрос на логин курьера {} не был отправлен, так как курьер не был создан", courier.getLogin());
            return null;
        }
    }

    @Step("Проверка логина клиента (кода статуса и значения `id` (не null))")
    public void loginCourierAndCheck(){
        CourierLoginResponseDto responseDto = loginCourier().then()
                .log().ifValidationFails()
                .statusCode(200)
                .extract().as(CourierLoginResponseDto.class);

        MatcherAssert.assertThat(responseDto.getId(), notNullValue());
        courier.setId(responseDto.getId());

        logger.debug("Успешный логин курьера {}, его id: {}", courier.getLogin(), courier.getId());
    }

    @Step("Удаление курьера")
    public Response deleteCourier(){

        Response response = given()
                .log().ifValidationFails()
                .delete("/api/v1/courier/" + courier.getId());

        logger.debug("Отправлен запрос на удаление курьера {}", courier.getLogin());
        return response;
    }

    @Step("Проверка удаления клиента (кода статуса и значения `ok`)")
    public void deleteCourierAndCheck(){
        deleteCourier().then()
                .log().ifValidationFails()
                .statusCode(200)
                .and()
                .assertThat().body("ok", equalTo(true));

        //log.debug("Успешное удаление курьера {}", courier.getLogin());
    }
}
