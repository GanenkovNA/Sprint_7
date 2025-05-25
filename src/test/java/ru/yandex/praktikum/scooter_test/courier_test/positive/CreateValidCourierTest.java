package ru.yandex.praktikum.scooter_test.courier_test.positive;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;


@DisplayName("Проверка возможности создания курьера с валидными данными")
public class CreateValidCourierTest extends CourierBase {

    @Before
    public void createValidCourier(){
        methodBeforeWithLog(
                this::createValidCourierEntity);
    }

    @Test
    @DisplayName("Создание курьера с валидными данными")
    public void createValidCourierTest() {
        methodTestWithLog(
                this::createValidCourierAndCheck);
    }

    @After
    public void cleanUp(){
        safeCleanUp(() -> {
            loginValidCourierAndCheck();
            deleteValidCourierAndCheck();
        });
    }
}
