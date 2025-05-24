package ru.yandex.praktikum.scooter.courier.positive;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import ru.yandex.praktikum.scooter.courier.CourierBase;

@DisplayName("Проверка возможности логина курьера с валидными данными")
public class LoginCourierTest extends CourierBase {

    @Before
    public void createNewCourier(){
        //log.info("{} - подготовка к тесту", this.getClass().getSimpleName());

        createValidCourierEntity();
        addNewCourierAndCheck();
    }

    @DisplayName("Логин курьера с валидными данными")
    //@Test
    public void loginCourierTest(){
        //log.info("{} - тест выполняется...", this.getClass().getSimpleName());

        loginCourierAndCheck();

        //log.info("{} - тест пройден", this.getClass().getSimpleName());
    }

    @After
    public void cleanUp(){
        deleteCourierAndCheck();
    }
}
