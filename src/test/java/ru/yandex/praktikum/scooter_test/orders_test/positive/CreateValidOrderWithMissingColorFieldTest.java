package ru.yandex.praktikum.scooter_test.orders_test.positive;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.infrastructure.rest_assured.ExchangeCaptureFilter;
import ru.yandex.praktikum.scooter.orders.dto.create.OrderCreationResponseDto;
import ru.yandex.praktikum.scooter_test.orders_test.OrdersBase;

import static org.hamcrest.CoreMatchers.notNullValue;

@DisplayName("Проверка возможности создания заказа без поля `color`")
public class CreateValidOrderWithMissingColorFieldTest extends OrdersBase {
    private final String[] color = null;
    private final int EXPECTED_STATUS_CODE = 201;
    private String orderWithMissingField;

    @Before
    public void createValidOrderWithMissingColorField(){
        methodBeforeWithLog(() -> {
            createDefaultOrderEntityWithCustomColor(color);
            logger.debug("Создана сущность заказа {}", order);

            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            try {
                orderWithMissingField = mapper.writeValueAsString(order);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @DisplayName("Проверка кода ответа (`" + EXPECTED_STATUS_CODE + "`)")
    @Test
    public void shouldCreateValidOrderWithMissingColorFieldAndVerifyStatusCode(){
        Response response = createOrderWithMissingFields(orderWithMissingField);
        createValidOrderWithMissingColorFieldAndVerify(response, () -> {
            assertStatusCode(response,
                    EXPECTED_STATUS_CODE,
                    getCurrentTestMethod());
        });
    }

    @DisplayName("Проверка параметра `track` в ответе")
    @Test
    public void shouldCreateValidOrderWithMissingColorFieldAndVerifyBodyParameterTrack(){
        Response response = createOrderWithMissingFields(orderWithMissingField);
        createValidOrderWithMissingColorFieldAndVerify(response, () -> {
            assertBody(
                    response,
                    "track",
                    notNullValue(),
                    "Ожидалось значение `track` не `null`",
                    getCurrentTestMethod());
        });
    }

    @After
    public void cleanUp(){
        safeCleanUp(() -> {
            getOrderByTrackAndVerify(getCurrentTestMethod());
            cancelOrderAndVerify(getCurrentTestMethod());
        });
    }

    private void createValidOrderWithMissingColorFieldAndVerify(Response response, Runnable check){
        methodTestWithLog(() -> {
            logger.debug("Отправлен запрос на создание заказа");

            check.run();

            OrderCreationResponseDto responseDto = response.as(OrderCreationResponseDto.class);
            order.setTrack(responseDto.getTrack());

            logger.debug("Создан заказ {}", order.getTrack());
        });
    }

    private Response createOrderWithMissingFields(String jsonBody){
        return RestAssured.given()
                .filter(new ExchangeCaptureFilter())
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .post("/api/v1/orders");
    }
}
