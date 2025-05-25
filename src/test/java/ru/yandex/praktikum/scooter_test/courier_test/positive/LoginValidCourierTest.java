package ru.yandex.praktikum.scooter_test.courier_test.positive;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;


@DisplayName("Проверка возможности логина курьера с валидными данными")
public class LoginValidCourierTest extends CourierBase {

    @Before
    public void createValidCourier(){
        methodBeforeWithLog(() -> {
            createValidCourierEntity();
            createValidCourierAndCheck();
        });
    }

    @DisplayName("Логин курьера с валидными данными")
    @Test
    public void loginCourierTest(){
        methodTestWithLog(
                this::loginValidCourierAndCheck);
    }

    @After
    public void cleanUp(){
        safeCleanUp(
                this::deleteValidCourierAndCheck);
    }
}
