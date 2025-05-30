package ru.yandex.praktikum.scooter_test.courier_test.positive;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.scooter.courier.dto.login.CourierLoginResponseDto;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

import static org.apache.http.HttpStatus.SC_OK;
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

    @DisplayName("Логин курьера с валидными данными и проверка ответа")
    @Description("Проверяются следующие параметры: \n" +
            "Код ответа - " + SC_OK + "\n" +
            "В теле содержится `id` (не `Null`)\n")
    @Test
    public void shouldLoginCourierTestAndCheckAndVerifyBodyParameterId(){
        methodTestWithLog(() -> {
            Response response = loginCourier(courier);

            logger.debug("Отправлен запрос на логин курьера {}", courier.getLogin());

            assertBody(response,
                    "id",
                    notNullValue(),
                    "Ожидалось значение `id` не `null`",
                    getCurrentTestMethod());

            assertStatusCode(response,
                    SC_OK,
                    getCurrentTestMethod());

            CourierLoginResponseDto responseDto = response.as(CourierLoginResponseDto.class);
            courier.setId(responseDto.getId());

            logger.debug("запрос на логин курьера {} успешен, его id - {}", courier.getLogin(), courier.getId());
        });
    }

    @After
    public void cleanUp(){
        safeCleanUp(
                this::deleteValidCourierAndVerify);
    }
}
