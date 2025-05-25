package ru.yandex.praktikum.scooter_test.courier_test.positive;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;

@DisplayName("Проверка возможности удаление курьера с валидными данными")
public class DeleteValidCourierTest extends CourierBase {
    private static final Logger logger = LoggerFactory.getLogger(DeleteValidCourierTest.class);

    @Before
    public void createValidCourierAndLogin(){
        logger.info("{} - подготовка к тесту...", getCurrentTestMethod());

        createValidCourierEntity();
        createValidCourierAndCheck();
        loginValidCourierAndCheck();

        logger.info("{} - подготовка к тесту завершена", getCurrentTestMethod());
    }

    @DisplayName("Удаление курьера с валидными данными")
    @Test
    public void deleteValidCourierTest(){
        logger.info("{} - тест выполняется...", getCurrentTestMethod());

        deleteValidCourierAndCheck();

        logger.info("{} - тест пройден", getCurrentTestMethod());
    }
}
