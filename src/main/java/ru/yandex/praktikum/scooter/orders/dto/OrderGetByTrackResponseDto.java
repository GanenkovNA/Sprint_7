package ru.yandex.praktikum.scooter.orders.dto;

import lombok.Data;

@Data
public class OrderGetByTrackResponseDto {
    private OrderGetByTrackResponseArrayDto order;
}
