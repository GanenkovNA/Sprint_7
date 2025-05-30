package ru.yandex.praktikum.scooter_test.orders_test.positive.create;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.scooter.orders.dto.create.OrderCreationResponseDto;
import ru.yandex.praktikum.scooter_test.orders_test.OrdersBase;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.yandex.praktikum.scooter_test.orders_test.OrdersService.createOrder;

@DisplayName("Проверка возможности создания заказа с цветом")
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

    @DisplayName("Создание сущности тестового заказа")
    @Before
    public void createValidOrderWithAnyColor(){
        methodBeforeWithLog(() -> {
            createDefaultOrderEntityWithCustomColor(color);
            logger.debug("Создана сущность заказа {}", order);
        });
    }

    @DisplayName("Создание заказа с цветом и проверка ответа")
    @Description("Проверяются следующие параметры: \n" +
            "Код ответа - " + SC_CREATED + "\n" +
            "В теле содержится `track != null`")
    @Test
    public void shouldCreateValidOrderWithAnyColorAndVerifyResponse(){
        methodTestWithLog(() -> {
            Response response = createOrder(order);

            logger.debug("Отправлен запрос на создание заказа");

            assertStatusCode(response,
                    SC_CREATED,
                    getCurrentTestMethod());

            assertBody(
                    response,
                    "track",
                    notNullValue(),
                    "Ожидалось значение `track` не `null`",
                    getCurrentTestMethod());

            OrderCreationResponseDto responseDto = response.as(OrderCreationResponseDto.class);
            order.setTrack(responseDto.getTrack());

            logger.debug("Создан заказ {}", order.getTrack());
        });
    }

    @After
    public void cleanUp(){
        safeCleanUp(() -> {
            getOrderByTrackAndVerify(getCurrentTestMethod());
            cancelOrderAndVerify(getCurrentTestMethod());
        });
    }
}
