package ru.yandex.praktikum.scooter_test.courier_test;

import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.MatcherAssert;
import ru.yandex.praktikum.infrastructure.rest_assured.ExchangeCaptureFilter;
import ru.yandex.praktikum.scooter.courier.dto.CourierEntity;
import ru.yandex.praktikum.scooter.courier.dto.login.CourierLoginResponseDto;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.praktikum.scooter_test.ScooterBase;


import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.*;
import static ru.yandex.praktikum.infrastructure.RandomStringGenerator.generateRandomString;
import static ru.yandex.praktikum.scooter_test.courier_test.CourierService.*;


public class CourierBase extends ScooterBase {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected CourierEntity courier;

    @Step("Создание сущности курьера с валидными данными")
    public void createValidCourierEntity(){
        courier = CourierEntity.builder()
                .login(generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING))
                .password(generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING))
                .firstName(generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING))
                .build();

        logger.debug("Создана сущность курьера: {}", courier);
    }

    @Step("Проверка создания валидного курьера (кода статуса `201` и значения `ok`)")
    public void createValidCourierAndVerify(){
        Response response = addNewCourier(courier);
        logger.debug("Отправлен запрос на создание курьера {}", courier.getLogin());

        assertStatusCode(response,
                SC_CREATED,
                "addNewValidCourierAndCheck");

        courier.setCreated(true);
        logger.debug("Успешное создание курьера {}", courier.getLogin());
    }

    public Response addNewCourierWithMissingFields(String jsonBody){
        return RestAssured.given()
                .filter(new ExchangeCaptureFilter())
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .post("/api/v1/courier");
    }

    @Step("Проверка логина валидного курьера (кода статуса `200` и значения `id` (не null))")
    public void loginValidCourierAndVerify(){
        loginCourierAndVerify(courier, "loginValidCourierAndVerify");
    }

    private void loginCourierAndVerify(CourierEntity loginCourier, String methodName){
        // Проверка, чтобы не отправлять заведомо невалидный запрос
        MatcherAssert.assertThat(
                String.format("Запрос на логин курьера %s не был отправлен, так как курьер не был создан", loginCourier.getLogin()),
                loginCourier.getCreated(),
                is(true));

        Response response = loginCourier(loginCourier);
        logger.debug("Отправлен запрос на логин курьера {}", loginCourier.getLogin());

        assertStatusCode(response,
                SC_OK,
                methodName);
        assertBody(response,
                "id",
                notNullValue(),
                "Ожидалось значение `id` не `null`",
                methodName);

        CourierLoginResponseDto responseDto = response.as(CourierLoginResponseDto.class);
        loginCourier.setId(responseDto.getId());

        logger.debug("Успешный логин курьера {}, его id: {}", loginCourier.getLogin(), loginCourier.getId());
    }

    @Step("Проверка удаления валидного курьера (кода статуса '200' и значения `ok`)")
    public void deleteValidCourierAndVerify(){
        deleteCourierAndVerify(courier, "deleteValidCourierAndCheck");
    }

    @Step("Проверка удаления курьера (кода статуса '200' и значения `ok`)")
    public void deleteAnyCourierAndVerify(CourierEntity anyCourier){
        deleteCourierAndVerify(anyCourier, "deleteAnyCourierAndVerify");
    }

    private void deleteCourierAndVerify(CourierEntity dropCourier, String methodName){
        // Проверка, чтобы не отправлять заведомо невалидный запрос
        MatcherAssert.assertThat(
                String.format("Запрос на удаление курьера %s не был отправлен, так как значение `id = null`", dropCourier.getLogin()),
                dropCourier.getId(),
                notNullValue()
        );

        Response response = deleteCourier(dropCourier);
        logger.debug("Отправлен запрос на удаление курьера {}", dropCourier.getLogin());

        assertStatusCode(response,
                SC_OK,
                methodName);
        assertBody(response,
                "ok",
                equalTo(true),
                "Ожидался `ok = true`",
                methodName);

        logger.debug("Успешное удаление курьера {}", dropCourier.getLogin());
    }

    @Step("Попытка аварийного удаления тестового клиента")
    protected void tryToDeleteInvalidCourierInCaseOfTestFail(CourierEntity invalidCourier){
        Response responseInvalidCourier = loginCourier(invalidCourier);
        if (responseInvalidCourier.getStatusCode() == 200){
            CourierLoginResponseDto responseDto = responseInvalidCourier.as(CourierLoginResponseDto.class);
            invalidCourier.setId(responseDto.getId());
            try {
                deleteAnyCourierAndVerify(invalidCourier);
                logger.debug("Невалидный курьер {} удалён", invalidCourier.getLogin());
                Allure.step("Невалидный курьер " + invalidCourier.getLogin() + " удалён");
            }catch (Throwable t){
                logger.error("Ошибка удаления невалидного курьера {}", invalidCourier.getLogin());
                Allure.step("Ошибка удаления невалидного курьера" + invalidCourier.getLogin());
            }
        }else{
            logger.debug("Удаление невалидного курьера {} не требуется", invalidCourier.getLogin());
            Allure.step("Удаление невалидного курьера " + invalidCourier.getLogin() + " не требуется");
        }
    }
}
