package ru.yandex.praktikum.scooter_test.courier_test.negative.login;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.praktikum.scooter_test.courier_test.CourierService.loginCourier;

@DisplayName("Проверка невозможности логина несуществующего курьера")
public class LoginNonExistentCourierTest extends CourierBase {
    private final int EXPECTED_STATUS_CODE = 404;
    private final String EXPECTED_MESSAGE = "Учетная запись не найдена";

    @DisplayName("Подготовка сущности курьера")
    @Before
    public void createNonExistentCourier(){
        methodBeforeWithLog(
                this::createValidCourierEntity);
    }

    @DisplayName("Проверка кода ответа (`" + EXPECTED_STATUS_CODE + "`)")
    @Test
    public void shouldNotLoginCourierWithInvalidPasswordAndVerifyStatusCode(){
        Response response = loginCourier(courier);
        loginNonExistentCourierAndVerify(() -> {
            assertStatusCode(response,
                    EXPECTED_STATUS_CODE,
                    getCurrentTestMethod());
        });
    }

    @DisplayName("Проверка параметра `message` в ответе")
    @Test
    public void shouldNotLoginCourierWithInvalidPasswordAndVerifyMessage(){
        Response response = loginCourier(courier);
        loginNonExistentCourierAndVerify(() -> {
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

    public void loginNonExistentCourierAndVerify(Runnable verify){
        methodTestWithLog(() -> {
            logger.debug("Отправлен запрос на логин несуществующего курьера");

            verify.run();
        });
    }
}
