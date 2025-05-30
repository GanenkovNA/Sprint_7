package ru.yandex.praktikum.scooter_test.orders_test.positive.get;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import ru.yandex.praktikum.scooter_test.orders_test.OrdersBase;

import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.yandex.praktikum.scooter_test.orders_test.OrdersService.getDefaultListOfOrders;

public class GetListOfOrdersTest extends OrdersBase {
    private final int EXPECTED_STATUS_CODE = 200;

    @DisplayName("Проверка кода ответа (`" + EXPECTED_STATUS_CODE + "`)")
    @Test
    public void shouldReturnListOfOrdersAndVerifyStatusCode(){
        Response response = getDefaultListOfOrders();
        returnListOfOrdersAndVerify(() -> {
            assertStatusCode(response,
                    EXPECTED_STATUS_CODE,
                    getCurrentTestMethod());
        });
    }

    @DisplayName("Проверка параметра `orders` в ответе")
    @Test
    public void shouldReturnListOfOrdersAndVerifyBodyParameterOrders(){
        Response response = getDefaultListOfOrders();
        returnListOfOrdersAndVerify(() -> {
            assertBody(
                    response,
                    "orders",
                    notNullValue(),
                    "Ожидалось значение `orders` не `null`",
                    getCurrentTestMethod());
        });
    }

    @DisplayName("Проверка параметров `pageInfo` в ответе")
    @Test
    public void shouldReturnListOfOrdersAndVerifyBodyParameterPageInfo(){
        Response response = getDefaultListOfOrders();
        returnListOfOrdersAndVerify(() -> {
            String[] jsonPathsOfPageInfoParameters = new String[]{
                    "pageInfo",
                    "pageInfo.page",
                    "pageInfo.total",
                    "pageInfo.limit"
            };

            for (String jsonPathsOfPageInfoParameter : jsonPathsOfPageInfoParameters) {
                assertBody(
                        response,
                        jsonPathsOfPageInfoParameter,
                        notNullValue(),
                        "Ожидалось значение `" + jsonPathsOfPageInfoParameter + "` не `null`",
                        getCurrentTestMethod());
            }
        });
    }

    @DisplayName("Проверка параметра `availableStations` в ответе")
    @Test
    public void shouldReturnListOfOrdersAndVerifyBodyParameterAvailableStations(){
        Response response = getDefaultListOfOrders();
        returnListOfOrdersAndVerify(() -> {
            assertBody(
                    response,
                    "availableStations",
                    notNullValue(),
                    "Ожидалось значение `availableStations` не `null`",
                    getCurrentTestMethod());
        });
    }

    private void returnListOfOrdersAndVerify(Runnable verify){
        methodTestWithLog(() -> {
            logger.debug("Отправлен запрос на получение списка заказов");

            verify.run();

            logger.debug("Список заказов получен");
        });
    }
}
