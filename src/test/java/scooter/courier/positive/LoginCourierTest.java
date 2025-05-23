package scooter.courier.positive;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import scooter.courier.CourierBase;

@DisplayName("Проверка возможности логина курьера с валидными данными")
public class LoginCourierTest extends CourierBase {
    @Before
    public void createNewCourier(){
        createValidCourierEntity();
        addNewCourierAndCheck();
    }

    @DisplayName("Логин курьера с валидными данными")
    @Test
    public void loginCourierTest(){
        loginCourierAndCheck();
    }

    @After
    public void cleanUp(){
        deleteCourierAndCheck();
    }
}
