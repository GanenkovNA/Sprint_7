package ru.yandex.praktikum.scooter_test.courier_test.negative.create;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.scooter.courier.dto.CourierEntity;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.praktikum.infrastructure.RandomStringGenerator.generateRandomString;
import static ru.yandex.praktikum.scooter_test.courier_test.CourierService.*;

@DisplayName("Проверка невозможности создания двух одинаковых курьеров")
public class CreateDuplicateOfCourierTest extends CourierBase {
    private CourierEntity courierDuplicate;

    private final String EXPECTED_MESSAGE_CREATE = "Этот логин уже используется";

    @Before
    public void createValidCourierAndCheckThrowLogin(){
        methodBeforeWithLog(() -> {
            createValidCourierEntity();
            createValidCourierAndVerify();
            loginValidCourierAndVerify();

            courierDuplicate = CourierEntity.builder()
                    .login(courier.getLogin())
                    .password(generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING))
                    .firstName(generateRandomString(DEFAULT_LENGTH_OF_GENERATED_STRING))
                    .build();
        });
    }

    @DisplayName("Попытка добавления дубликата курьера и проверка ответа")
    @Description("Проверяются следующие параметры: \n" +
            "Код ответа - " + SC_CONFLICT + "\n" +
            "В теле содержится `message = " + EXPECTED_MESSAGE_CREATE + "`")
    @Test
    public void shouldNotCreateDuplicateOfCourierAndVerifyResponse(){
        methodTestWithLog(() -> {
            Response response = addNewCourier(courierDuplicate);

            logger.debug("Отправлен запрос на создание дубликата курьера {}", courierDuplicate.getLogin());

            try {
                assertStatusCode(response,
                        SC_CONFLICT,
                        getCurrentTestMethod());

                assertBody(response,
                        "message",
                        equalTo(EXPECTED_MESSAGE_CREATE),
                        "Ожидалось сообщение `\"message\": \""+ EXPECTED_MESSAGE_CREATE + "\"`",
                        getCurrentTestMethod());

                logger.debug("Дубликат курьера {} успешно не создан", courierDuplicate.getLogin());

            }catch (Throwable e) {
                logger.debug("Попытка удаления дубликата курьера {}", courierDuplicate.getLogin());
                tryToDeleteInvalidCourierInCaseOfTestFail(courierDuplicate);
                throw e;
            }
        });
    }

    @After
    public void cleanUp(){
        safeCleanUp(this::deleteValidCourierAndVerify);
    }
}
