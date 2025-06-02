package ru.yandex.praktikum.scooter_test.courier_test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.yandex.praktikum.infrastructure.rest_assured.ExchangeCaptureFilter;
import ru.yandex.praktikum.scooter.courier.dto.create.CourierCreationRequestDto;
import ru.yandex.praktikum.scooter.courier.dto.CourierEntity;
import ru.yandex.praktikum.scooter.courier.dto.login.CourierLoginRequestDto;
import ru.yandex.praktikum.infrastructure.rest_assured.ApiClient;

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

    public static Response addNewCourierWithMissingFields(String jsonBody){
        return RestAssured.given()
                .filter(new ExchangeCaptureFilter())
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .post("/api/v1/courier");
    }

    public static Response loginCourier(CourierEntity courier){
        CourierLoginRequestDto request = CourierLoginRequestDto.builder()
                .login(courier.getLogin())
                .password(courier.getPassword())
                .build();

        return ApiClient.post(
                "/api/v1/courier/login",
                request,
                "Логин курьера " + courier.getLogin());
    }

    public static Response loginCourierWithMissingFields(String courierWithMissingFieldAsString){
        return ApiClient.post(
                "/api/v1/courier/login",
                courierWithMissingFieldAsString,
                "Логин курьера без поля");
    }

    public static Response deleteCourier(CourierEntity courier) {

        return ApiClient.delete(
                "/api/v1/courier/" + courier.getId(),
                "Удаление клиента " + courier.getId()
        );
    }
}
