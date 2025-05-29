package ru.yandex.praktikum.scooter.orders.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class OrderEntity {
    //для создания заказа
    @Builder.Default
    private String firstName = "Ваня";
    @Builder.Default
    private String lastName = "Борисыч";
    @Builder.Default
    private String address = "Ленинский проспект, 1";
    @Builder.Default
    private String metroStation = "1";
    @Builder.Default
    private String phone = "+7 800 355 35 35";
    @Builder.Default
    private Integer rentTime = 1;
    private String deliveryDate;
    @Builder.Default
    private String comment = "Some comment";
    @Builder.Default
    private String[] color = {"BLACK"};

    //для ответа при создании заказа
    private String track;

    // для ответа при получении заказа по track
    private Boolean cancelled;
}
