package ru.yandex.praktikum.scooter_test.courier_test.positive.create;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

import static org.apache.http.HttpStatus.SC_CREATED;
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
    @DisplayName("Создание курьера с валидными данными и проверка ответа")
    @Description("Проверяются следующие параметры: \n" +
            "Код ответа - " + SC_CREATED + "\n" +
            "В теле содержится `ok = true`\n")
    public void shouldCreateValidCourierAndVerifyResponse() {
        methodTestWithLog(() -> {
            Response response = addNewCourier(courier);
            logger.debug("Отправлен запрос на создание курьера {}", courier.getLogin());

            assertStatusCode(response,
                    SC_CREATED,
                    getCurrentTestMethod());

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
    @Description("При создании курьера он должен быть добавлен в базу (проверяется через логин)")
    public void shouldCreateValidCourierAndLogin(){
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
