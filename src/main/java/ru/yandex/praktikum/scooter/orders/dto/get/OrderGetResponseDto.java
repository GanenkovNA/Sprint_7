package ru.yandex.praktikum.scooter.orders.dto.get;

public class OrderGetResponseDto {
    private OrderGetResponseArrayDto[] orders;
    private PageInfoDTO pageInfo;
    private AvailableStationsDTO[] availableStations;
}
