package ru.yandex.praktikum.scooter_test.courier_test.negative.login;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.scooter.courier.dto.CourierEntity;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.praktikum.scooter_test.courier_test.CourierService.loginCourier;

@DisplayName("Проверка невозможности логина курьера с null полем")
@RunWith(Parameterized.class)
public class LoginCourierWithNullFieldTest extends CourierBase {
    private final String login;
    private final String password;

    private CourierEntity courierWithMissingField;

    private final int EXPECTED_STATUS_CODE = 400;
    private final String EXPECTED_MESSAGE = "Недостаточно данных для входа";

    public LoginCourierWithNullFieldTest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters(name = "login={0}, password={1}")
    public static Object[][] testData(){
        return new Object[][] {
                {null, "1"},
                {"1", null}
        };
    }

    @DisplayName("Подготовка запроса курьера с null полем")
    @Before
    public void createCourierEntityWithMissingField(){
        methodBeforeWithLog(() -> {

            createValidCourierEntity();
            createValidCourierAndVerify();
            loginValidCourierAndVerify();

            courierWithMissingField = courier.toBuilder()
                    .login(login != null ? courier.getLogin() : null)
                    .password(password != null ? courier.getPassword() : null)
                    .firstName(null)
                    .created(null)
                    .build();


        });
    }

    @DisplayName("Проверка кода ответа (`" + EXPECTED_STATUS_CODE + "`)")
    @Test
    public void shouldNotLoginCourierWithNullFieldAndVerifyStatusCode() {
        methodTestWithLog(() -> {
            Response response = loginCourier(courierWithMissingField);
            loginCourierWithNullFieldAndVerify(() -> {
                assertStatusCode(response,
                        EXPECTED_STATUS_CODE,
                        getCurrentTestMethod());
            });
        });
    }

    @DisplayName("Проверка параметра `message` в ответе")
    @Test
    public void shouldNotCreateCourierWithNullFieldAndVerifyMessage(){
        methodTestWithLog(() -> {
            Response response = loginCourier(courierWithMissingField);
            loginCourierWithNullFieldAndVerify(() -> {
                assertBody(response,
                        "message",
                        equalTo(EXPECTED_MESSAGE),
                        "Ожидалось сообщение `\"message\": \""+ EXPECTED_MESSAGE + "\"`",
                        getCurrentTestMethod());
            });
        });
    }

    @After
    public void cleanUP(){
        safeCleanUp(
                this::loginValidCourierAndVerify);
    }

    private void loginCourierWithNullFieldAndVerify(Runnable verify){
        methodTestWithLog(() -> {
            logger.debug("Отправлен запрос на логин курьера {}", courier.getLogin());

            verify.run();
        });
    }
}
