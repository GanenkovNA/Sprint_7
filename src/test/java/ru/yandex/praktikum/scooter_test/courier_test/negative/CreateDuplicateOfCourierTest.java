package ru.yandex.praktikum.scooter_test.courier_test.negative;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.scooter.courier.dto.CourierEntity;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.praktikum.infrastructure.RandomStringGenerator.generateRandomString;
import static ru.yandex.praktikum.scooter_test.courier_test.CourierService.*;

@DisplayName("Проверка невозможности создания двух одинаковых курьеров")
public class CreateDuplicateOfCourierTest extends CourierBase {
    private CourierEntity courierDuplicate;

    private final int EXPECTED_STATUS_CODE_CREATE = 409;
    private final String EXPECTED_MESSAGE_CREATE = "Этот логин уже используется";
    private final int EXPECTED_STATUS_CODE_LOGIN = 404;

    @Before
    public void createValidCourierAndCheckThrowLogin(){
        methodBeforeWithLog(() -> {
            createValidCourierEntity();
            createValidCourierAndVerify();
            loginValidCourierAndVerify();

            courierDuplicate = CourierEntity.builder()
                    .login(courier.getLogin())
                    .password(generateRandomString(5))
                    .firstName(generateRandomString(5))
                    .build();
        });
    }

    @DisplayName("Проверка кода ответа (`409`)")
    @Test
    public void createDuplicateOfCourierAndVerifyStatusCode(){
        Response response = addNewCourier(courierDuplicate);
        verifyCreateResponseWithLog(() -> {
            assertStatusCode(response,
                    EXPECTED_STATUS_CODE_CREATE,
                    getCurrentTestMethod());
        });
    }

    @DisplayName("Проверка параметра `message` в ответе")
    @Test
    public void createDuplicateOfCourierAndVerifyBodyParameterMessage(){
        Response response = addNewCourier(courierDuplicate);
        verifyCreateResponseWithLog(() -> {
            assertBody(response,
                    "message",
                    equalTo(EXPECTED_MESSAGE_CREATE),
                    "Ожидалось сообщение `\"message\": \""+ EXPECTED_MESSAGE_CREATE + "\"`",
                    getCurrentTestMethod());
        });
    }

    @DisplayName("Проверка возможности авторизации дубликатом курьера")
    @Test
    public void loginDuplicateOfCourierAndVerifyStatusCode(){
        addNewCourier(courierDuplicate);
        Response response = loginCourier(courierDuplicate);
        verifyLoginResponseWithLog(() -> {
            assertStatusCode(response,
                    EXPECTED_STATUS_CODE_LOGIN,
                    getCurrentTestMethod());
        });
    }

    @After
    public void cleanUp(){
        safeCleanUp(this::deleteValidCourierAndVerify);
    }

    private void verifyCreateResponseWithLog(Runnable verify){
        logger.debug("Отправлен запрос на создание дубликата курьера {}", courierDuplicate.getLogin());
        verifyResponse(verify);
    }

    private void verifyLoginResponseWithLog(Runnable verify){
        logger.debug("Отправлен запрос на логин дубликата курьера {}", courierDuplicate.getLogin());
        verifyResponse(verify);
    }

    private void verifyResponse(Runnable verify){
        methodTestWithLog(() -> {
            try {
                verify.run();
            }catch (Throwable e) {
                deleteDuplicateOfCourier();
                throw e;
            }
        });
    }

    private void deleteDuplicateOfCourier(){
        logger.debug("Попытка удаления дубликата курьера {}", courierDuplicate.getLogin());
        tryToDeleteInvalidCourierInCaseOfTestFail(courierDuplicate);
    }
}
