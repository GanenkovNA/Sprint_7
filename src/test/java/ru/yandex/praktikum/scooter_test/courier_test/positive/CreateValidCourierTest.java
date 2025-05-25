package ru.yandex.praktikum.scooter_test.courier_test.positive;

import io.qameta.allure.Allure;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;


@DisplayName("Проверка возможности создания курьера с валидными данными")
public class CreateValidCourierTest extends CourierBase {
    private static final Logger logger = LoggerFactory.getLogger(CreateValidCourierTest.class);

    @Before
    public void createNewCourier(){
        logger.info("{} - подготовка к тесту", getCurrentTestMethod());

        createValidCourierEntity();
    }

    @Test
    @DisplayName("Создание курьера с валидными данными")
    public void createValidCourierTest() {
        logger.info("{} - тест выполняется...", getCurrentTestMethod());

        createValidCourierAndCheck();

        logger.info("{} - тест пройден", getCurrentTestMethod());
    }

    @After
    public void cleanUp(){
        try {
            loginValidCourierAndCheck();
            deleteValidCourierAndCheck();
        } catch (Throwable e) {
            logger.warn("Очистка после теста завершилась с ошибкой: \n{}", e.getMessage());
            Allure.step("Очистка после теста завершилась с ошибкой (НЕ влияет на результат теста): \n" + e.getMessage());
            // не бросаем исключение — тест считается успешным
        }
    }
}
