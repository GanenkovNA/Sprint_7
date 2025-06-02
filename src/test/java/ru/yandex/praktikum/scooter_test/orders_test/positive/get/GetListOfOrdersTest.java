package ru.yandex.praktikum.scooter_test.orders_test.positive.get;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import ru.yandex.praktikum.scooter_test.orders_test.OrdersBase;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;
import static ru.yandex.praktikum.scooter_test.orders_test.OrdersService.getDefaultListOfOrders;

@DisplayName("Проверка возможности получения списка заказов без указания необязательных полей")
public class GetListOfOrdersTest extends OrdersBase {

    @DisplayName("Запрос списка заказов без указания необязательных полей и проверка ответа")
    @Description("Проверяются следующие параметры: \n" +
            "Код ответа - " + SC_OK + "\n" +
            "В теле содержатся `orders`, `pageInfo{page, total, limit}` и `availableStations`, не содержащие `null`")
    @Test
    public void shouldReturnListOfOrdersAndVerifyResponse(){
        methodTestWithLog(() -> {
            Response response = getDefaultListOfOrders();

            logger.debug("Отправлен запрос на получение списка заказов");

            assertStatusCode(response,
                    SC_OK,
                    getCurrentTestMethod());

            String[] jsonPathsOfPageInfoParameters = new String[]{
                    "orders",
                    "pageInfo",
                    "pageInfo.page",
                    "pageInfo.total",
                    "pageInfo.limit",
                    "availableStations",
            };

            for (String jsonPathsOfPageInfoParameter : jsonPathsOfPageInfoParameters) {
                assertBody(
                        response,
                        jsonPathsOfPageInfoParameter,
                        notNullValue(),
                        "Ожидалось значение `" + jsonPathsOfPageInfoParameter + "` не `null`",
                        getCurrentTestMethod());
            }

            logger.debug("Список заказов получен");
        });
    }
}
