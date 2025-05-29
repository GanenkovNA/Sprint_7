package ru.yandex.praktikum.scooter_test.orders_test;

import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.praktikum.scooter.orders.dto.OrderEntity;
import ru.yandex.praktikum.scooter.orders.dto.get.OrderGetByTrackResponseDto;
import ru.yandex.praktikum.scooter_test.ScooterBase;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.yandex.praktikum.infrastructure.DateStringGenerator.getTomorrowDateString;
import static ru.yandex.praktikum.scooter_test.orders_test.OrdersService.cancelOrder;
import static ru.yandex.praktikum.scooter_test.orders_test.OrdersService.getOrderByTrack;

public class OrdersBase extends ScooterBase {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected OrderEntity order;

    public void createDefaultOrderEntityWithCustomColor(String[] color){
        order = OrderEntity.builder()
                .deliveryDate(getTomorrowDateString())
                .color(color)
                .build();
    }

    public void getOrderByTrackAndVerify(String methodName){
        // Проверка, чтобы не отправлять заведомо невалидный запрос
        MatcherAssert.assertThat(
                "Запрос на получение данных по заказу не был отправлен, так как значение `track = null`",
                order.getTrack(),
                notNullValue()
        );

        Response response = getOrderByTrack(order);
        assertStatusCode(response,
                200,
                methodName);

        OrderGetByTrackResponseDto responseDto = response.as(OrderGetByTrackResponseDto.class);
        order.setCancelled(responseDto.getOrder().getCancelled());
        logger.debug("Успешное получение данных по заказу {}", order.getTrack());
    }

    public void cancelOrderAndVerify(String methodName){
        // Проверка, чтобы не отправлять заведомо невалидный запрос
        MatcherAssert.assertThat(
                "Запрос на удаление заказа не был отправлен, так как значение `cancelled != false`",
                order.getCancelled(),
                equalTo(false)
        );

        Response response = cancelOrder(order);
        assertStatusCode(response,
                200,
                methodName);

        logger.debug("Успешная отмена заказа {}", order.getTrack());
    }
}
