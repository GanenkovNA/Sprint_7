package ru.yandex.praktikum.scooter.orders.dto.get;

import lombok.Data;

@Data
public class OrderGetByTrackResponseDto {
    private OrderGetResponseArrayDto order;
}
