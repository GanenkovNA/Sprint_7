package ru.yandex.praktikum.scooter_test.courier_test.negative.create;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.scooter.courier.dto.CourierEntity;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

import static org.hamcrest.CoreMatchers.equalTo;
import static ru.yandex.praktikum.infrastructure.RandomStringGenerator.generateRandomString;
import static ru.yandex.praktikum.scooter_test.courier_test.CourierService.addNewCourier;


@RunWith(Parameterized.class)
public class CreateCourierWithNullFieldTest extends CourierBase {
    private final String login;
    private final String password;
    private final String firstName;

    private final int EXPECTED_STATUS_CODE = 400;
    private final String EXPECTED_MESSAGE = "Недостаточно данных для создания учетной записи";

    public CreateCourierWithNullFieldTest(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Parameterized.Parameters(name = "login={0}, password={1}, firstName={2}")
    public static Object[][] testData(){
        return new Object[][] {
                {null, generateRandomString(5), generateRandomString(5)},
                {generateRandomString(5), null, generateRandomString(5)},
                {generateRandomString(5), generateRandomString(5), null}
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
        });
    }

    @DisplayName("Проверка кода ответа (`" + EXPECTED_STATUS_CODE + "`)")
    @Test
    public void shouldNotCreateCourierWithMissingFieldAndVerifyStatusCode() {
        Response response = addNewCourier(courier);
        createCourierWithNullFieldAndVerify(() -> {
            assertStatusCode(response,
                    EXPECTED_STATUS_CODE,
                    getCurrentTestMethod());
        });
    }

    @DisplayName("Проверка параметра `message` в ответе")
    @Test
    public void shouldNotCreateCourierWithMissingFieldAndVerifyMessage(){
        Response response = addNewCourier(courier);
        createCourierWithNullFieldAndVerify(() -> {
            assertBody(response,
                    "message",
                    equalTo(EXPECTED_MESSAGE),
                    "Ожидалось сообщение `\"message\": \""+ EXPECTED_MESSAGE + "\"`",
                    getCurrentTestMethod());
        });
    }

    private void createCourierWithNullFieldAndVerify(Runnable verify){
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
}
