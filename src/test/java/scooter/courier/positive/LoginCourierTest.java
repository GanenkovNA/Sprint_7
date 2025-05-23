package scooter.courier.positive;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import scooter.courier.CourierBase;

public class LoginCourierTest extends CourierBase {
    @Before
    public void createNewCourier(){
        createValidCourierEntity();
        addNewCourierAndCheck();
    }

    @Test
    public void loginCourierTest(){
        loginCourierAndCheck();
    }

    @After
    public void cleanUp(){
        deleteCourierAndCheck();
    }
}
