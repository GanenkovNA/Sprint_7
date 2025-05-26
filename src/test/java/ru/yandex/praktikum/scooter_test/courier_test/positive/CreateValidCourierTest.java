package ru.yandex.praktikum.scooter_test.courier_test.positive;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.praktikum.scooter_test.courier_test.CourierService.addNewCourier;


@DisplayName("Проверка возможности создания курьера с валидными данными")
public class CreateValidCourierTest extends CourierBase {

    @Before
    public void createValidCourierEntityForTest(){
        methodBeforeWithLog(
                this::createValidCourierEntity);
    }

    @Test
    @DisplayName("Проверка кода ответа (`201`)")
    public void createValidCourierAndVerifyStatusCode() {
        methodTestWithLog(
                this::createValidCourierAndVerify);
    }

    @Test
    @DisplayName("Проверка параметра `ok` (`true`) в ответе")
    public void createValidCourierAndVerifyBodyParameterOk() {
        methodTestWithLog(() -> {
            Response response = addNewCourier(courier);
            logger.debug("Отправлен запрос на создание курьера {}", courier.getLogin());

            assertBody(response,
                    "ok",
                    equalTo(true),
                    "Ожидался `ok = true`",
                    getCurrentTestMethod());
            logger.debug("Успешное создание курьера {}", courier.getLogin());

            courier.setCreated(true);
        });
    }

    @Test
    @DisplayName("Проверка создания курьера через логин")
    public void createValidCourierAndLogin(){
        methodTestWithLog(() -> {
            createValidCourierAndVerify();
            loginValidCourierAndVerify();
        });
    }

    @After
    public void cleanUp(){
        safeCleanUp(() -> {
            loginValidCourierAndVerify();
            deleteValidCourierAndVerify();
        });
    }
}
