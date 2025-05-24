package ru.yandex.praktikum.scooter_test.courier_test.positive;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;


@DisplayName("Проверка возможности логина курьера с валидными данными")
public class LoginCourierTest extends CourierBase {

    @Before
    public void createNewCourier(){
        //log.info("{} - подготовка к тесту", this.getClass().getSimpleName());

        createValidCourierEntity();
        createValidCourierAndCheck();
    }

    @DisplayName("Логин курьера с валидными данными")
    //@Test
    public void loginCourierTest(){
        //log.info("{} - тест выполняется...", this.getClass().getSimpleName());

        loginValidCourierAndCheck();

        //log.info("{} - тест пройден", this.getClass().getSimpleName());
    }

    @After
    public void cleanUp(){
        deleteValidCourierAndCheck();
    }
}
