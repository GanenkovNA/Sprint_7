package scooter.courier.positive;

import org.junit.Before;
import org.junit.Test;
import scooter.courier.CourierBase;

public class DeleteCourierTest extends CourierBase {

    @Before
    public void createNewCourier(){
        createValidCourierEntity();
        addNewCourierAndCheck();
        loginCourierAndCheck();
    }

    @Test
    public void deleteCourierTest(){
        deleteCourierAndCheck();
    }
}
