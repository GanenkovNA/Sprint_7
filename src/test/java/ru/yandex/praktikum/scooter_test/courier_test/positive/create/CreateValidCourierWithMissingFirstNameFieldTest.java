package ru.yandex.praktikum.scooter_test.courier_test.positive.create;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.scooter.courier.dto.CourierEntity;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.praktikum.infrastructure.RandomStringGenerator.generateRandomString;

public class CreateValidCourierWithMissingFirstNameFieldTest extends CourierBase {

    private String courierWithMissingFirstNameField;

    @Before
    public void createValidCourierEntityWithMissingFirstName(){
        methodBeforeWithLog(() -> {
            courier = CourierEntity.builder()
                    .login(generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING))
                    .password(generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING))
                    .build();

            logger.debug("Создана сущность курьера без поля `firstName`: {}", courier);

            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            try {
                courierWithMissingFirstNameField = mapper.writeValueAsString(courier);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    @DisplayName("Создание курьера с валидными данными и пропущенным полем `firstName` и проверка ответа")
    @Description("Проверяются следующие параметры: \n" +
            "Код ответа - " + SC_CREATED + "\n" +
            "В теле содержится `ok = true`\n")
    public void shouldCreateValidCourierWithMissingFirstNameAndVerify(){
        methodTestWithLog(() -> {
            Response response = addNewCourierWithMissingFields(courierWithMissingFirstNameField);

            assertStatusCode(response,
                    SC_CREATED,
                    getCurrentTestMethod());

            assertBody(response,
                    "ok",
                    equalTo(true),
                    "Ожидался `ok = true`",
                    getCurrentTestMethod());

            logger.debug("Успешное создание курьера {}", courier.getLogin());

            courier.setCreated(true);
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
