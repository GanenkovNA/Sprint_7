package ru.yandex.praktikum.scooter.courier.positive;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.scooter.courier.CourierBase;

@DisplayName("Проверка возможности создания курьера с валидными данными")
public class AddCourierTest extends CourierBase {


    @Before
    public void createNewCourier(){
        //log.info("{} - подготовка к тесту", this.getClass().getSimpleName());

        createValidCourierEntity();
    }

    @Test
    @DisplayName("Создание курьера с валидными данными")
    public void addNewCourierTest() {
        //log.info("{} - тест выполняется...", this.getClass().getSimpleName());

        addNewCourierAndCheck();

        //log.info("{} - тест пройден", this.getClass().getSimpleName());
    }

    @After
    public void cleanUp(){
        loginCourierAndCheck();
        deleteCourierAndCheck();
    }
}
