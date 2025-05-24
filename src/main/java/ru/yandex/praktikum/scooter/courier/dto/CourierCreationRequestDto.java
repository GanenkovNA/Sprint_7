package ru.yandex.praktikum.scooter.courier.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierCreationRequestDto {
    private String login;
    private String password;
    private String firstName;
}
