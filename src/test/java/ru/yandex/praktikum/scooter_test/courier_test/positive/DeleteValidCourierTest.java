package ru.yandex.praktikum.scooter_test.courier_test.positive;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

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

    @DisplayName("Проверка кода ответа (`200`)")
    @Test
    public void deleteValidCourierAndVerifyStatusCode(){
        Response response = deleteCourier(courier);
        verifyResponseWithLog(() -> {
            assertStatusCode(response,
                    200,
                    "deleteValidCourierAndCheck");
        });
    }

    @DisplayName("Проверка параметра `ok = true` в ответе")
    @Test
    public void deleteValidCourierAndVerifyBodyParameterOk(){
        Response response = deleteCourier(courier);
        verifyResponseWithLog(() -> {
            assertBody(response,
                    "ok",
                    equalTo(true),
                    "Ожидался `ok = true`",
                    "deleteValidCourierAndCheck");
        });
    }

    private void verifyResponseWithLog(Runnable verify){
        methodTestWithLog(() -> {
            logger.debug("Отправлен запрос на удаление курьера {}", courier.getLogin());

            verify.run();

            logger.debug("Успешное удаление курьера {}", courier.getLogin());
        });
    }

}
