package ru.yandex.praktikum.scooter.orders.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCreationRequestDto {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private Integer rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;
}
