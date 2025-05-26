package ru.yandex.praktikum.scooter.courier.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierEntity {
    private String login;
    private String password;
    private String firstName;
    @Builder.Default
    private Boolean created = false;
    private Integer id;
}
