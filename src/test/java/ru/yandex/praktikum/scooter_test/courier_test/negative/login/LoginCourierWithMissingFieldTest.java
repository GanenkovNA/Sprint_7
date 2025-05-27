package ru.yandex.praktikum.scooter_test.courier_test.negative.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.infrastructure.rest_assured.ApiClient;
import ru.yandex.praktikum.scooter.courier.dto.CourierEntity;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Проверка невозможности логина курьера с отсутствующим полем")
@RunWith(Parameterized.class)
public class LoginCourierWithMissingFieldTest extends CourierBase{
    private final String login;
    private final String password;

    private CourierEntity courierWithMissingField;
    private String courierWithMissingFieldAsString;

    private final int EXPECTED_STATUS_CODE = 400;
    private final String EXPECTED_MESSAGE = "Недостаточно данных для входа";

    public LoginCourierWithMissingFieldTest(String login, String password) {
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

    @DisplayName("Подготовка запроса курьера с отсутствующим полем")
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

            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            try {
                courierWithMissingFieldAsString = mapper.writeValueAsString(courierWithMissingField);
            } catch (JsonProcessingException e) {
                logger.error("Some error? {}", String.valueOf(e));
            }
        });
    }

    @DisplayName("Проверка кода ответа (`" + EXPECTED_STATUS_CODE + "`)")
    @Test
    public void shouldNotLoginCourierWithMissingFieldAndVerifyStatusCode() {
        methodTestWithLog(() -> {
            Response response = loginCourierWithMissingFields();
            loginCourierWithMissingFieldAndVerify(() -> {
                assertStatusCode(response,
                        EXPECTED_STATUS_CODE,
                        getCurrentTestMethod());
            });
        });
    }

    @DisplayName("Проверка параметра `message` в ответе")
    @Test
    public void shouldNotCreateCourierWithMissingFieldAndVerifyMessage(){
        Response response = loginCourierWithMissingFields();
        loginCourierWithMissingFieldAndVerify(() -> {
            assertBody(response,
                    "message",
                    equalTo(EXPECTED_MESSAGE),
                    "Ожидалось сообщение `\"message\": \""+ EXPECTED_MESSAGE + "\"`",
                    getCurrentTestMethod());
        });
    }

    @After
    public void cleanUP(){
        safeCleanUp(
                this::loginValidCourierAndVerify);
    }

    private void loginCourierWithMissingFieldAndVerify(Runnable verify){
        methodTestWithLog(() -> {
            logger.debug("Отправлен запрос на логин курьера {}", courier.getLogin());

            verify.run();
        });
    }

    //Нужен для передачи строки
    private Response loginCourierWithMissingFields(){
        return ApiClient.post(
                "/api/v1/courier/login",
                courierWithMissingFieldAsString,
                "Логин курьера без поля");
    }
}
