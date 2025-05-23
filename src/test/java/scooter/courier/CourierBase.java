package scooter.courier;

import courier.CourierEntity;
import courier.dto.CourierCreationRequestDto;
import courier.dto.CourierLoginRequestDto;
import courier.dto.CourierLoginResponseDto;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.Before;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierBase {
    protected CourierEntity courier;

    @Before
    public void setUpUri() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Step("Создание сущности курьера с валидными данными")
    public void createValidCourierEntity(){
        courier = CourierEntity.builder()
                .login("bobr")
                .password("dobr")
                .firstName("superman")
                .build();
    }

    @Step("Создание курьера")
    public Response addNewCourier() {
        CourierCreationRequestDto request = CourierCreationRequestDto.builder()
                .login(courier.getLogin())
                .password(courier.getPassword())
                .firstName(courier.getFirstName())
                .build();

        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/v1/courier");
    }

    @Step("Проверка создания клиента (кода статуса и значения `ok`)")
    public void addNewCourierAndCheck(){
        Response response = addNewCourier();

        response.then()
                .statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));
    }

    @Step("Логин курьера")
    public Response loginCourier(){
        CourierLoginRequestDto request = CourierLoginRequestDto.builder()
                .login(courier.getLogin())
                .password(courier.getPassword())
                .build();

        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/v1/courier/login");
    }

    @Step("Проверка логина клиента (кода статуса и значения `id` (не null))")
    public void loginCourierAndCheck(){
        CourierLoginResponseDto responseDto = loginCourier().then()
                .statusCode(200)
                .extract().as(CourierLoginResponseDto.class);

        MatcherAssert.assertThat(responseDto.getId(), notNullValue());
        courier.setId(responseDto.getId());
    }

    @Step("Удаление курьера")
    public Response deleteCourier(){
        return given()
                .delete("/api/v1/courier/" + courier.getId());
    }

    @Step("Проверка удаления клиента (кода статуса и значения `ok`)")
    public void deleteCourierAndCheck(){
        deleteCourier().then()
                .statusCode(200)
                .and()
                .assertThat().body("ok", equalTo(true));
    }
}
