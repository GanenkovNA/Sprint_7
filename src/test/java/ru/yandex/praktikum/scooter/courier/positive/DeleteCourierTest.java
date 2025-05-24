package ru.yandex.praktikum.scooter.courier.positive;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import ru.yandex.praktikum.scooter.courier.CourierBase;

@DisplayName("Проверка возможности удаление курьера с валидными данными")
public class DeleteCourierTest extends CourierBase {


    @Before
    public void createNewCourier(){
        //log.info("{} - подготовка к тесту", this.getClass().getSimpleName());

        createValidCourierEntity();
        addNewCourierAndCheck();
        loginCourierAndCheck();
    }

    @DisplayName("Удаление курьера с валидными данными")
    //@Test
    public void deleteCourierTest(){
        //log.info("{} - тест выполняется...", this.getClass().getSimpleName());

        deleteCourierAndCheck();

        //log.info("{} - тест пройден", this.getClass().getSimpleName());
    }
}
