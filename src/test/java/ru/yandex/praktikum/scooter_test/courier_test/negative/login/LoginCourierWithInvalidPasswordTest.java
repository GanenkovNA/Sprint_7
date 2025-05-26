package ru.yandex.praktikum.scooter_test.courier_test.negative.login;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.scooter.courier.dto.CourierEntity;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.praktikum.infrastructure.RandomStringGenerator.generateRandomString;
import static ru.yandex.praktikum.scooter_test.courier_test.CourierService.loginCourier;

@DisplayName("Проверка невозможности логина курьера с неверным паролем")
public class LoginCourierWithInvalidPasswordTest extends CourierBase {
    private CourierEntity courierWithInvalidPassword;

    private final int EXPECTED_STATUS_CODE = 404;
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

    @DisplayName("Проверка кода ответа (`" + EXPECTED_STATUS_CODE + "`)")
    @Test
    public void shouldNotLoginCourierWithInvalidPasswordAndVerifyStatusCode(){
        Response response = loginCourier(courierWithInvalidPassword);
        loginCourierWithInvalidPasswordAndVerify(() -> {
            assertStatusCode(response,
                    EXPECTED_STATUS_CODE,
                    getCurrentTestMethod());
        });
    }

    @DisplayName("Проверка параметра `message` в ответе")
    @Test
    public void shouldNotLoginCourierWithInvalidPasswordAndVerifyMessage(){
        Response response = loginCourier(courierWithInvalidPassword);
        loginCourierWithInvalidPasswordAndVerify(() -> {
            assertBody(response,
                    "message",
                    equalTo(EXPECTED_MESSAGE),
                    "Ожидалось сообщение `\"message\": \""+ EXPECTED_MESSAGE + "\"`",
                    getCurrentTestMethod());
        });
    }

    @After
    public void cleanUp(){
        safeCleanUp(() -> {
            loginValidCourierAndVerify();
            deleteValidCourierAndVerify();
        });
    }

    public void loginCourierWithInvalidPasswordAndVerify(Runnable verify){
        methodTestWithLog(() -> {
            logger.debug("Отправлен запрос на логин курьера c неверным паролем");

            verify.run();
        });
    }
}
