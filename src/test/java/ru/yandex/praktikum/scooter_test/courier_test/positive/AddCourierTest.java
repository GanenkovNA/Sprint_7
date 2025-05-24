package ru.yandex.praktikum.scooter_test.courier_test.positive;

import io.qameta.allure.Allure;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.praktikum.scooter_test.ScooterBase;
import ru.yandex.praktikum.scooter_test.courier_test.CourierBase;


@DisplayName("Проверка возможности создания курьера с валидными данными")
public class AddCourierTest extends CourierBase {
    private static final Logger logger = LoggerFactory.getLogger(AddCourierTest.class);

    @Before
    public void createNewCourier(){
        //log.info("{} - подготовка к тесту", this.getClass().getSimpleName());

        createValidCourierEntity();
    }

    @Test
    @DisplayName("Создание курьера с валидными данными")
    public void addNewCourierTest() {
        //log.info("{} - тест выполняется...", this.getClass().getSimpleName());

        createValidCourierAndCheck();

        //log.info("{} - тест пройден", this.getClass().getSimpleName());
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
