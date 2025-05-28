package ru.yandex.praktikum.scooter_test.orders_test;

import io.restassured.response.Response;
import ru.yandex.praktikum.infrastructure.rest_assured.ApiClient;
import ru.yandex.praktikum.scooter.orders.dto.OrderCancelRequestDto;
import ru.yandex.praktikum.scooter.orders.dto.OrderCreationRequestDto;
import ru.yandex.praktikum.scooter.orders.dto.OrderEntity;

public class OrdersService {
    public static Response createOrder(OrderEntity order){
        OrderCreationRequestDto request = OrderCreationRequestDto.builder()
                .firstName(order.getFirstName())
                .lastName(order.getLastName())
                .address(order.getAddress())
                .metroStation(order.getMetroStation())
                .phone(order.getPhone())
                .rentTime(order.getRentTime())
                .deliveryDate(order.getDeliveryDate())
                .comment(order.getComment())
                .color(order.getColor())
                .build();

        return ApiClient.post(
                "/api/v1/orders",
                request,
                "Попытка создания заказа");
    }

    public static Response cancelOrder(OrderEntity order){
        OrderCancelRequestDto request = OrderCancelRequestDto.builder()
                .track(order.getTrack())
                .build();

        return ApiClient.put(
                "/api/v1/orders/cancel",
                request,
                "Попытка отмены заказа " + order.getTrack()
        );
    }
}
