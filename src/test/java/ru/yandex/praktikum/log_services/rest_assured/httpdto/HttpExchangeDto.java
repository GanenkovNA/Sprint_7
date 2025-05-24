package ru.yandex.praktikum.log_services.rest_assured.httpdto;

import lombok.Builder;

@Builder
public class HttpExchangeDto {
    private HttpRequestDto request;
    private HttpResponseDto response;

    @Override
    public String toString() {
        return request.toString() + "\n" + response.toString();
    }

    // геттеры, если нужно

}
