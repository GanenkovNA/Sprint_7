package ru.yandex.praktikum.scooter_test.courier_test.positive;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.praktikum.scooter_test.courier_test.CourierService.deleteCourier;

@DisplayName("Проверка возможности удаление курьера с валидными данными")
public class DeleteValidCourierTest extends CourierBase {

    @Before
    public void createValidCourierAndLogin(){
        methodBeforeWithLog(() -> {
            createValidCourierEntity();
            createValidCourierAndVerify();
            loginValidCourierAndVerify();
        });
    }

    @DisplayName("Удаление курьера с валидными данными и проверка ответа")
    @Description("Проверяются следующие параметры: \n" +
            "Код ответа - " + SC_OK + "\n" +
            "В теле содержится `ok = true`\n")
    @Test
    public void shouldDeleteValidCourierAndVerifyResponse(){
        methodTestWithLog(() -> {
            Response response = deleteCourier(courier);

            logger.debug("Отправлен запрос на удаление курьера {}", courier.getLogin());

            assertStatusCode(response,
                    SC_OK,
                    "deleteValidCourierAndCheck");

            assertBody(response,
                    "ok",
                    equalTo(true),
                    "Ожидался `ok = true`",
                    "deleteValidCourierAndCheck");

            logger.debug("Успешное удаление курьера {}", courier.getLogin());
        });
    }
}
