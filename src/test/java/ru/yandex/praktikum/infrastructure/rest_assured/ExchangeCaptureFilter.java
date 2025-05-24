package ru.yandex.praktikum.infrastructure.rest_assured;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import ru.yandex.praktikum.infrastructure.rest_assured.dto.http.HttpExchangeDto;
import ru.yandex.praktikum.infrastructure.rest_assured.dto.http.HttpRequestDto;
import ru.yandex.praktikum.infrastructure.rest_assured.dto.http.HttpResponseDto;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ExchangeCaptureFilter implements Filter {
    // ThreadLocal для потокобезопасности
    private static final ThreadLocal<HttpExchangeDto> lastExchange = new ThreadLocal<>();

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {

        long startTime = System.currentTimeMillis();
        Response response = ctx.next(requestSpec, responseSpec);
        long responseTime = System.currentTimeMillis() - startTime;

        // Преобразуем тело запроса к отформатированной строке
        String requestBody = extractPrettyBody(requestSpec.getBody());

        HttpRequestDto requestDto = HttpRequestDto.builder()
                .method(requestSpec.getMethod())
                .url(requestSpec.getURI())
                .headers(toSimpleMap(requestSpec.getHeaders()))
                .cookies(toSimpleMap(requestSpec.getCookies()))
                .body(requestBody)
                .build();

        HttpResponseDto responseDto = HttpResponseDto.builder()
                .statusCode(response.getStatusCode())
                .statusText(stripStatusText(response.getStatusLine()))
                .responseTimeMs(responseTime)
                .headers(toSimpleMap(response.getHeaders()))
                .body(parseResponseBody(response))
                .build();

        HttpExchangeDto exchange = HttpExchangeDto.builder()
                .request(requestDto)
                .response(responseDto)
                .build();

        lastExchange.set(exchange);

        return response;
    }

    public static HttpExchangeDto getLastExchange() {
        return lastExchange.get();
    }

    private Map<String, String> toSimpleMap(Headers headers) {
        return headers.asList().stream()
                .collect(Collectors.toMap(
                        Header::getName,
                        Header::getValue,
                        (a, b) -> b,
                        LinkedHashMap::new));
    }

    private Map<String, String> toSimpleMap(Cookies cookies) {
        return cookies.asList().stream()
                .collect(Collectors.toMap(
                        Cookie::getName,
                        Cookie::getValue,
                        (a, b) -> b,
                        LinkedHashMap::new));
    }

    private Object parseResponseBody(Response response) {
        String contentType = response.getContentType();
        if (contentType != null && contentType.contains("application/json")) {
            try {
                return new ObjectMapper().readValue(response.getBody().asString(), Object.class);
            } catch (JsonProcessingException e) {
                // Не удалось распарсить JSON — возвращаем как строку
                return response.getBody().asString();
            }
        }
        return response.getBody().asString();
    }

    private String extractPrettyBody(Object body) {
        if (body == null) return null;

        ObjectMapper mapper = new ObjectMapper();

        try {
            if (body instanceof String) {
                String bodyStr = (String) body;
                Object json = mapper.readValue(bodyStr, Object.class);
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            } else {
                return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
            }
        } catch (Exception e) {
            return body.toString();
        }
    }



    private String stripStatusText(String statusLine) {
        // Удаляем префикс "HTTP/1.1 200 " чтобы осталась только текстовая часть
        return statusLine.replaceFirst("^HTTP/\\d\\.\\d \\d+ ", "");
    }
}
