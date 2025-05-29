package ru.yandex.praktikum.scooter.orders.dto.get;

import lombok.Data;

@Data
public class PageInfoDTO {
    Integer page;
    Integer total;
    Integer limit;
}
