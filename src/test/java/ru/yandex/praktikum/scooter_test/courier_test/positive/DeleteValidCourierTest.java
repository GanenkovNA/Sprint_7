package ru.yandex.praktikum.scooter_test.courier_test.positive;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

@DisplayName("Проверка возможности удаление курьера с валидными данными")
public class DeleteValidCourierTest extends CourierBase {

    @Before
    public void createValidCourierAndLogin(){
        methodBeforeWithLog(() -> {
            createValidCourierEntity();
            createValidCourierAndCheck();
            loginValidCourierAndCheck();
        });
    }

    @DisplayName("Удаление курьера с валидными данными")
    @Test
    public void deleteValidCourierTest(){
        methodTestWithLog(
                this::deleteValidCourierAndCheck);
    }
}
