package ru.yandex.praktikum.scooter_test.courier_test.positive;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.scooter.courier.dto.CourierLoginResponseDto;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.yandex.praktikum.scooter_test.courier_test.CourierService.loginCourier;


@DisplayName("Проверка возможности логина курьера с валидными данными")
public class LoginValidCourierTest extends CourierBase {

    @Before
    public void createValidCourier(){
        methodBeforeWithLog(() -> {
            createValidCourierEntity();
            createValidCourierAndVerify();
        });
    }

    @DisplayName("Проверка кода ответа (`200`)")
    @Test
    public void loginCourierAndVerifyStatusCode(){
        Response response = loginCourier(courier);
        verifyResponseWithLog(response, () -> {
            assertStatusCode(response,
                    200,
                    getCurrentTestMethod());
        });
    }

    @DisplayName("Проверка параметра `id` (не `Null`) в ответе")
    @Test
    public void loginCourierTestAndCheckAndVerifyBodyParameterId(){
        Response response = loginCourier(courier);
        verifyResponseWithLog(response, () -> {
            assertBody(response,
                    "id",
                    notNullValue(),
                    "Ожидалось значение `id` не `null`",
                    getCurrentTestMethod());
        });
    }

    @After
    public void cleanUp(){
        safeCleanUp(
                this::deleteValidCourierAndVerify);
    }

    private void verifyResponseWithLog(Response response, Runnable verify){
        methodTestWithLog(() -> {
            logger.debug("Отправлен запрос на логин курьера {}", courier.getLogin());

            verify.run();

            CourierLoginResponseDto responseDto = response.as(CourierLoginResponseDto.class);
            courier.setId(responseDto.getId());
        });
    }
}
