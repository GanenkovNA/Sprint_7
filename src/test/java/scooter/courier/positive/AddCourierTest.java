package scooter.courier.positive;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import scooter.courier.CourierBase;

public class AddCourierTest extends CourierBase {

    @Before
    public void createNewCourier(){
        createValidCourierEntity();
    }

    @Test
    public void addNewCourierTest() {
        addNewCourierAndCheck();
    }

    @After
    public void cleanUp(){
        loginCourierAndCheck();
        deleteCourierAndCheck();
    }
}
