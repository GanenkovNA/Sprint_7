package ru.yandex.praktikum.scooter_test.courier_test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import ru.yandex.praktikum.scooter.courier.dto.CourierCreationRequestDto;
import ru.yandex.praktikum.scooter.courier.dto.CourierEntity;
import ru.yandex.praktikum.scooter.courier.dto.CourierLoginRequestDto;
import ru.yandex.praktikum.infrastructure.rest_assured.ExchangeCaptureFilter;

import static io.restassured.RestAssured.given;


public class CourierService {
    public static Response addNewCourier(CourierEntity courier) {
        CourierCreationRequestDto request = CourierCreationRequestDto.builder()
                .login(courier.getLogin())
                .password(courier.getPassword())
                .firstName(courier.getFirstName())
                .build();

        return RestAssured.given()
                .filter(new ExchangeCaptureFilter())
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/v1/courier");
    }

    public static Response loginCourier(CourierEntity courier){
        CourierLoginRequestDto request = CourierLoginRequestDto.builder()
                .login(courier.getLogin())
                .password(courier.getPassword())
                .build();
            return RestAssured.given()
                    .filter(new ExchangeCaptureFilter())
                    .contentType(ContentType.JSON)
                    .body(request)
                    .post("/api/v1/courier/login");

    }

    public static Response deleteCourier(CourierEntity courier){

        return given()
                .filter(new ExchangeCaptureFilter())
                .delete("/api/v1/courier/" + courier.getId());
    }
}
