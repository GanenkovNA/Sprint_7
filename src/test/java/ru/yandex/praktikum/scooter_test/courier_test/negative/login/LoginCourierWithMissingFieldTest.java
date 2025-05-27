package ru.yandex.praktikum.scooter_test.courier_test.negative.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.infrastructure.rest_assured.ExchangeCaptureFilter;
import ru.yandex.praktikum.scooter.courier.dto.CourierEntity;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.praktikum.infrastructure.RandomStringGenerator.generateRandomString;

@DisplayName("Проверка невозможности логина курьера с отсутствующим полем")
@RunWith(Parameterized.class)
public class LoginCourierWithMissingFieldTest extends CourierBase {
    private final String login;
    private final String password;

    private String courierWithMissingFieldAsString;
    CourierEntity courierWithMissingField;
    private final int EXPECTED_STATUS_CODE = 400;
    private final String EXPECTED_MESSAGE = "Недостаточно данных для входа";

    public LoginCourierWithMissingFieldTest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters(name = "login={0}, password={1}")
    public static Object[][] testData(){
        return new Object[][] {
                {null, generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING)},
                {generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING), null}
        };
    }

    @DisplayName("Подготовка запроса курьера с отсутствующим полем")
    @Before
    public void createCourierEntityWithMissingField() {
        methodBeforeWithLog(() -> {
            if (login == null){
                courier = CourierEntity.builder()
                        .login(generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING))
                        .password(password)
                        .firstName(generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING))
                        .build();
                courierWithMissingField = courier.toBuilder()
                        .login(login)
                        .firstName(null)
                        .created(null)
                        .build();
            } else if (password == null) {
                courier = CourierEntity.builder()
                        .login(login)
                        .password(generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING))
                        .firstName(generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING))
                        .build();
                courierWithMissingField = courier.toBuilder()
                        .password(password)
                        .firstName(null)
                        .created(null)
                        .build();
            }

            createValidCourierAndVerify();

            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            try {
                courierWithMissingFieldAsString = mapper.writeValueAsString(courierWithMissingField);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @DisplayName("Проверка кода ответа (`" + EXPECTED_STATUS_CODE + "`)")
    @Test
    public void shouldNotLoginCourierWithMissingFieldAndVerifyStatusCode() {
        Response response = loginCourierWithMissingFields(courierWithMissingFieldAsString);
        loginCourierWithMissingFieldAndVerify(() -> {
            assertStatusCode(response,
                    EXPECTED_STATUS_CODE,
                    getCurrentTestMethod());
        });
    }

    @DisplayName("Проверка параметра `message` в ответе")
    @Test
    public void shouldNotCreateCourierWithMissingFieldAndVerifyMessage(){
        Response response = loginCourierWithMissingFields(courierWithMissingFieldAsString);
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
        safeCleanUp(() -> {
            loginValidCourierAndVerify();
            deleteValidCourierAndVerify();
        });
    }

    private void loginCourierWithMissingFieldAndVerify(Runnable verify){
        methodTestWithLog(() -> {
            logger.debug("Отправлен запрос на логин курьера {}", courier.getLogin());

            try{
                verify.run();
            } catch (Throwable e) {
                tryToDeleteInvalidCourierInCaseOfTestFail(courier);
                throw e;
            }
        });
    }

    private Response loginCourierWithMissingFields(String jsonBody){
        return RestAssured.given()
                .filter(new ExchangeCaptureFilter())
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .post("/api/v1/courier/login");
    }
}
