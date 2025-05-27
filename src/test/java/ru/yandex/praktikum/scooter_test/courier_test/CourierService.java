package ru.yandex.praktikum.scooter_test.courier_test;

import io.restassured.response.Response;
import ru.yandex.praktikum.scooter.courier.dto.CourierCreationRequestDto;
import ru.yandex.praktikum.scooter.courier.dto.CourierEntity;
import ru.yandex.praktikum.scooter.courier.dto.CourierLoginRequestDto;
import ru.yandex.praktikum.infrastructure.rest_assured.ApiClient;

import static io.restassured.RestAssured.given;


public class CourierService {
    public static Response addNewCourier(CourierEntity courier) {
        CourierCreationRequestDto request = CourierCreationRequestDto.builder()
                .login(courier.getLogin())
                .password(courier.getPassword())
                .firstName(courier.getFirstName())
                .build();

        return ApiClient.post(
                "/api/v1/courier",
                request,
                "Создание курьера " + courier.getLogin());
    }

    public static Response loginCourier(CourierEntity courier){
        CourierLoginRequestDto request = CourierLoginRequestDto.builder()
                .login(courier.getLogin())
                .password(courier.getPassword())
                .build();

        return ApiClient.post(
                "",
                request,
                "Логин курьера " + courier.getLogin());
    }

    public static Response deleteCourier(CourierEntity courier) {

        return ApiClient.delete(
                "/api/v1/courier/" + courier.getId(),
                "Удаление клиента " + courier.getId()
        );
    }
}
