package ru.yandex.praktikum.courier;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourierEntity {
    private String login;
    private String password;
    private String firstName;
    private boolean isCreated = false;
    private Integer id;
}
