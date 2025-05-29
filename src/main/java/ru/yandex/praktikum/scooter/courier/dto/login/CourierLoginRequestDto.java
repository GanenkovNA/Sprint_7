package ru.yandex.praktikum.scooter.courier.dto.login;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierLoginRequestDto {
    String login;
    String password;
}
