package ru.yandex.praktikum.scooter_test.courier_test.positive;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;


@DisplayName("Проверка возможности логина курьера с валидными данными")
public class LoginValidCourierTest extends CourierBase {
    private static final Logger logger = LoggerFactory.getLogger(LoginValidCourierTest.class);

    @Before
    public void createValidCourier(){
        logger.info("{} - подготовка к тесту", getCurrentTestMethod());

        createValidCourierEntity();
        createValidCourierAndCheck();

        logger.info("{} - подготовка к тесту завершена", getCurrentTestMethod());
    }

    @DisplayName("Логин курьера с валидными данными")
    @Test
    public void loginCourierTest(){
        logger.info("{} - тест выполняется...", getCurrentTestMethod());

        loginValidCourierAndCheck();

        logger.info("{} - тест пройден", getCurrentTestMethod());
    }

    @After
    public void cleanUp(){
        try{
            logger.info("{} - очистка после теста...", getCurrentTestMethod());

            deleteValidCourierAndCheck();

            logger.info("{} - очистка после теста завершена", getCurrentTestMethod());
        }catch (Throwable e){
            cleanUpCatch(e);
        }
    }
}
