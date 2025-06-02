package ru.yandex.praktikum.scooter.orders.dto.cancel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCancelRequestDto {
    private String track;
}
