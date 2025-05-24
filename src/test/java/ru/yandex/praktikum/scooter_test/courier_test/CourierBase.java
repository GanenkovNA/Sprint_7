package ru.yandex.praktikum.scooter_test.courier_test;

import org.hamcrest.MatcherAssert;
import ru.yandex.praktikum.scooter.courier.dto.CourierEntity;
import ru.yandex.praktikum.scooter.courier.dto.CourierLoginResponseDto;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.praktikum.scooter_test.ScooterBase;


import static org.hamcrest.CoreMatchers.*;
import static ru.yandex.praktikum.scooter_test.courier_test.CourierService.*;


public class CourierBase extends ScooterBase {
    private static final Logger logger = LoggerFactory.getLogger(CourierBase.class);

    protected CourierEntity courier;

    @Step("Создание сущности курьера с валидными данными")
    public void createValidCourierEntity(){
        courier = CourierEntity.builder()
                .login("bobr")
                .password("null")
                .firstName("superman")
                .build();

        logger.debug("Создана сущность курьера: {}", courier);
    }

    @Step("Проверка создания валидного курьера (кода статуса `201` и значения `ok`)")
    public void createValidCourierAndCheck(){
        Response response = addNewCourier(courier);

        logger.debug("Отправлен запрос на создание курьера {}", courier.getLogin());

        assertStatusCode(response, 201, "addNewValidCourierAndCheck");

        assertBody(response, "ok", (Matcher<?>) equalTo(true), "Ожидался `ok = true`", "addNewValidCourierAndCheck");

        courier.setCreated(true);
        logger.debug("Успешное создание курьера {}", courier.getLogin());
    }

    @Step("Проверка логина валидного курьера (кода статуса `200` и значения `id` (не null))")
    public void loginValidCourierAndCheck(){
        // Проверка, чтобы не отправлять заведомо невалидный запрос
        MatcherAssert.assertThat(
                String.format("Запрос на логин курьера %s не был отправлен, так как курьер не был создан", courier.getLogin()),
                courier.isCreated(),
                is(false));

        logger.debug("Отправлен запрос на логин курьера {}", courier.getLogin());

        Response response = loginCourier(courier);
        assertStatusCode(response, 200, "loginValidCourierAndCheck");
        assertBody(response, "id", notNullValue(), "Ожидалось значение `id` не `null`", "loginValidCourierAndCheck");

        CourierLoginResponseDto responseDto = response.as(CourierLoginResponseDto.class);
        courier.setId(responseDto.getId());

        logger.debug("Успешный логин курьера {}, его id: {}", courier.getLogin(), courier.getId());
    }

    @Step("Проверка удаления валидного курьера (кода статуса '200' и значения `ok`)")
    public void deleteValidCourierAndCheck(){
        // Проверка, чтобы не отправлять заведомо невалидный запрос
        try {
            MatcherAssert.assertThat(
                    String.format("Запрос на удаление курьера %s не был отправлен, так как значение `id = null`", courier.getLogin()),
                    courier.getId(),
                    notNullValue()
            );
        } catch (AssertionError e){
            throw e;
        }

        logger.debug("Отправлен запрос на удаление курьера {}", courier.getLogin());

        Response response = deleteCourier(courier);
        assertStatusCode(response, 200, "deleteValidCourierAndCheck");
        assertBody(response, "ok", equalTo(true), "Ожидался `ok = true`", "deleteValidCourierAndCheck");

        logger.debug("Успешное удаление курьера {}", courier.getLogin());
    }
}
