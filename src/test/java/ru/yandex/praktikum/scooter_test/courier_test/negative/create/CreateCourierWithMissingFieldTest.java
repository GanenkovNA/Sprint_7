package ru.yandex.praktikum.scooter_test.courier_test.negative.create;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.scooter.courier.dto.CourierEntity;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.praktikum.infrastructure.RandomStringGenerator.generateRandomString;
import static ru.yandex.praktikum.scooter_test.courier_test.CourierService.addNewCourierWithMissingFields;

@DisplayName("Проверка невозможности создания курьера с отсутствующим полем")
@RunWith(Parameterized.class)
public class CreateCourierWithMissingFieldTest extends CourierBase {
    private final String login;
    private final String password;
    private final String firstName;

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
                {generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING), null, generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING)}
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

            logger.debug("Создана сущность курьера c без поля: {}", courier);

            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            try {
                courierWithMissingField = mapper.writeValueAsString(courier);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @DisplayName("Попытка создания курьера с отсутствующим полем и проверка ответа")
    @Description("Проверяются следующие параметры: \n" +
            "Код ответа - " + SC_BAD_REQUEST + "\n" +
            "В теле содержится `message = " + EXPECTED_MESSAGE + "`")
    @Test
    public void shouldNotCreateCourierWithMissingFieldAndVerifyResponse(){
        methodTestWithLog(() -> {
            Response response = addNewCourierWithMissingFields(courierWithMissingField);

            logger.debug("Отправлен запрос на создание курьера {}", courier.getLogin());

            try{
                assertStatusCode(response,
                        SC_BAD_REQUEST,
                        getCurrentTestMethod());

                assertBody(response,
                        "message",
                        equalTo(EXPECTED_MESSAGE),
                        "Ожидалось сообщение `\"message\": \""+ EXPECTED_MESSAGE + "\"`",
                        getCurrentTestMethod());
            } catch (Throwable e) {
                tryToDeleteInvalidCourierInCaseOfTestFail(courier);
                throw e;
            }

            logger.debug("Курьер {} успешно не был создан", courier.getLogin());
        });
    }
}
