package ru.yandex.praktikum.infrastructure.rest_assured.dto.http;

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
