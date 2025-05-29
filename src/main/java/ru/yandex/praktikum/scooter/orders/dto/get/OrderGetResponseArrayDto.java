package ru.yandex.praktikum.scooter.orders.dto.get;

import lombok.Data;

@Data
public class OrderGetResponseArrayDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private Integer rentTime;
    private String deliveryDate;
    private Integer track;
    private Integer status;
    private String[] color;
    private String comment;
    private Boolean cancelled;
    private Boolean finished;
    private Boolean inDelivery;
    private String courierFirstName;
    private String createdAt;
    private String updatedAt;
}
