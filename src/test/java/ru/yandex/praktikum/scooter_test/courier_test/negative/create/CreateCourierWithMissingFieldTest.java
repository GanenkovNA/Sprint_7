package ru.yandex.praktikum.scooter_test.courier_test.negative.create;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.infrastructure.rest_assured.ExchangeCaptureFilter;
import ru.yandex.praktikum.scooter.courier.dto.CourierEntity;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.praktikum.infrastructure.RandomStringGenerator.generateRandomString;

@DisplayName("Проверка невозможности создания курьера с отсутствующим полем")
@RunWith(Parameterized.class)
public class CreateCourierWithMissingFieldTest extends CourierBase {
    private final String login;
    private final String password;
    private final String firstName;

    private final int EXPECTED_STATUS_CODE = 400;
    private final String EXPECTED_MESSAGE = "Недостаточно данных для создания учетной записи";

    private String courierWithMissingField;

    public CreateCourierWithMissingFieldTest(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Parameterized.Parameters(name = "login={0}, password={1}, firstName={2}")
    public static Object[][] testData(){
        return new Object[][] {
                {null, generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING), generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING)},
                {generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING), null, generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING)},
                {generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING), generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING), null}
        };
    }

    @DisplayName("Подготовка курьера с отсутствующим полем")
    @Before
    public void createCourierEntityWithMissingField() {
        methodBeforeWithLog(() -> {
            courier = CourierEntity.builder()
                    .login(login)
                    .password(password)
                    .firstName(firstName)
                    .created(null)
                    .build();

            logger.debug("Создана сущность курьера: {}", courier);

            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            try {
                courierWithMissingField = mapper.writeValueAsString(courier);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @DisplayName("Проверка кода ответа (`" + EXPECTED_STATUS_CODE + "`)")
    @Test
    public void shouldNotCreateCourierWithMissingFieldAndVerifyStatusCode() {
        Response response = addNewCourierWithMissingFields(courierWithMissingField);
        createCourierWithMissingFieldAndVerify(() -> {
            assertStatusCode(response,
                    EXPECTED_STATUS_CODE,
                    getCurrentTestMethod());
        });
    }

    @DisplayName("Проверка параметра `message` в ответе")
    @Test
    public void shouldNotCreateCourierWithMissingFieldAndVerifyMessage(){
        Response response = addNewCourierWithMissingFields(courierWithMissingField);
        createCourierWithMissingFieldAndVerify(() -> {
            assertBody(response,
                    "message",
                    equalTo(EXPECTED_MESSAGE),
                    "Ожидалось сообщение `\"message\": \""+ EXPECTED_MESSAGE + "\"`",
                    getCurrentTestMethod());
        });
    }

    private void createCourierWithMissingFieldAndVerify(Runnable verify){
        methodTestWithLog(() -> {
            logger.debug("Отправлен запрос на создание курьера {}", courier.getLogin());

            try{
                verify.run();
            } catch (Throwable e) {
                tryToDeleteInvalidCourierInCaseOfTestFail(courier);
                throw e;
            }
        });
    }

    private Response addNewCourierWithMissingFields(String jsonBody){
        return RestAssured.given()
                .filter(new ExchangeCaptureFilter())
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .post("/api/v1/courier");
    }
}
