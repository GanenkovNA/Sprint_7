package ru.yandex.praktikum.scooter_test.orders_test.positive;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.scooter.orders.dto.OrderCreationResponseDto;
import ru.yandex.praktikum.scooter_test.orders_test.OrdersBase;

import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.yandex.praktikum.scooter_test.orders_test.OrdersService.createOrder;

@DisplayName("Проверка возможности создания заказа с одним цветом")
@RunWith(Parameterized.class)
public class CreateValidOrderWithAnyColorTest extends OrdersBase {
    private final String[] color;

    public CreateValidOrderWithAnyColorTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "color={0}")
    public static Object[][] testData(){
        return new Object[][] {
                {new String[]{"GREY"}},
                {new String[]{"BLACK"}},
                {new String[]{"GREY", "BLACK"}},
                {new String[]{}}
        };
    }

    @Before
    public void createValidOrderWithOnlyColor(){
        methodBeforeWithLog(() -> {
            createDefaultOrderEntityWithCustomColor(color);
            logger.debug("Создана сущность заказа {}", order);
        });
    }

    @Test
    public void shouldCreateValidOrderWithOnlyColorAndVerifyStatusCode(){
        Response response = createOrder(order);
        createValidOrderWithOnlyColorAndVerify(response, () -> {
            assertStatusCode(response,
                    201,
                    getCurrentTestMethod());
        });
    }

    @Test
    public void shouldCreateValidOrderWithOnlyColorAndVerifyBodyParameterTrack(){
        Response response = createOrder(order);
        createValidOrderWithOnlyColorAndVerify(response, () -> {
            assertBody(
                    response,
                    "track",
                    notNullValue(),
                    "Ожидалось значение `track` не `null`",
                    getCurrentTestMethod());
        });
    }

    @After
    public void cleanUp(){
        safeCleanUp(() -> {
            getOrderByTrackAndVerify(getCurrentTestMethod());
            cancelOrderAndVerify(getCurrentTestMethod());
        });
    }

    private void createValidOrderWithOnlyColorAndVerify(Response response, Runnable check){
        methodTestWithLog(() -> {
            logger.debug("Отправлен запрос на создание заказа");

            check.run();

            OrderCreationResponseDto responseDto = response.as(OrderCreationResponseDto.class);
            order.setTrack(responseDto.getTrack());

            logger.debug("Создан заказ {}", order.getTrack());
        });
    }
}
