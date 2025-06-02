package ru.yandex.praktikum.scooter_test.courier_test.negative.login;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.scooter.courier.dto.CourierEntity;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.praktikum.infrastructure.RandomStringGenerator.generateRandomString;
import static ru.yandex.praktikum.scooter_test.courier_test.CourierService.loginCourier;

@DisplayName("Проверка невозможности логина курьера с неверным паролем")
public class LoginCourierWithInvalidPasswordTest extends CourierBase {
    private CourierEntity courierWithInvalidPassword;

    private final String EXPECTED_MESSAGE = "Учетная запись не найдена";

    @DisplayName("Подготовка курьера с неверным паролем")
    @Before
    public void createCourierAndInvalidPassword(){
        methodBeforeWithLog(() -> {
            createValidCourierEntity();
            createValidCourierAndVerify();
            courierWithInvalidPassword = courier.toBuilder()
                    .password(generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING))
                    .build();
            logger.debug("Создана сущность курьера c неверным паролем: {}", courier);
        });
    }

    @DisplayName("Попытка логина курьера с неверным паролем и проверка ответа")
    @Description("Проверяются следующие параметры: \n" +
            "Код ответа - " + SC_NOT_FOUND + "\n" +
            "В теле содержится `message = " + EXPECTED_MESSAGE + "`")
    @Test
    public void shouldNotLoginCourierWithInvalidPasswordAndVerifyResponse(){
        methodTestWithLog(() -> {
            Response response = loginCourier(courierWithInvalidPassword);

            logger.debug("Отправлен запрос на логин курьера c неверным паролем");

            assertStatusCode(response,
                    SC_NOT_FOUND,
                    getCurrentTestMethod());

            assertBody(response,
                    "message",
                    equalTo(EXPECTED_MESSAGE),
                    "Ожидалось сообщение `\"message\": \""+ EXPECTED_MESSAGE + "\"`",
                    getCurrentTestMethod());

            logger.debug("Логин курьера успешно не был совершён");
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
