package ru.yandex.praktikum.scooter.orders.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCancelRequestDto {
    private String track;
}
