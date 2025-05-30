package ru.yandex.praktikum.scooter_test.courier_test.negative.login;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.praktikum.scooter_test.courier_test.CourierService.loginCourier;

@DisplayName("Проверка невозможности логина несуществующего курьера")
public class LoginNonExistentCourierTest extends CourierBase {
    private final String EXPECTED_MESSAGE = "Учетная запись не найдена";

    @DisplayName("Подготовка сущности курьера")
    @Before
    public void createNonExistentCourier(){
        methodBeforeWithLog(
                this::createValidCourierEntity);
    }

    @DisplayName("Попытка логина курьера с `null` полем и проверка ответа")
    @Description("Проверяются следующие параметры: \n" +
            "Код ответа - " + SC_NOT_FOUND + "\n" +
            "В теле содержится `message = " + EXPECTED_MESSAGE + "`")
    @Test
    public void shouldNotLoginCourierWithInvalidPasswordAndVerifyStatusCode(){
        methodTestWithLog(() -> {
            Response response = loginCourier(courier);

            logger.debug("Отправлен запрос на логин несуществующего курьера");

            assertStatusCode(response,
                    SC_NOT_FOUND,
                    getCurrentTestMethod());
            assertBody(response,
                    "message",
                    equalTo(EXPECTED_MESSAGE),
                    "Ожидалось сообщение `\"message\": \""+ EXPECTED_MESSAGE + "\"`",
                    getCurrentTestMethod());

            logger.debug("Логин несуществующего курьера успешно не был совершён");
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
