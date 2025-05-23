package scooter.courier.positive;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import scooter.courier.CourierBase;

@DisplayName("Проверка возможности удаление курьера с валидными данными")
public class DeleteCourierTest extends CourierBase {

    @Before
    public void createNewCourier(){
        createValidCourierEntity();
        addNewCourierAndCheck();
        loginCourierAndCheck();
    }

    @DisplayName("Удаление курьера с валидными данными")
    @Test
    public void deleteCourierTest(){
        deleteCourierAndCheck();
    }
}
