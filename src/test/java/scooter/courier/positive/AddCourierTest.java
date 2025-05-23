package scooter.courier.positive;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import scooter.courier.CourierBase;

@DisplayName("Проверка возможности создания курьера с валидными данными")
public class AddCourierTest extends CourierBase {

    @Before
    public void createNewCourier(){
        createValidCourierEntity();
    }

    @Test
    @DisplayName("Создание курьера с валидными данными")
    public void addNewCourierTest() {
        addNewCourierAndCheck();
    }

    @After
    public void cleanUp(){
        loginCourierAndCheck();
        deleteCourierAndCheck();
    }
}
