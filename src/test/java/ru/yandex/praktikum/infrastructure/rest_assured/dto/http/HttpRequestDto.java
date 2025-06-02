package ru.yandex.praktikum.infrastructure.rest_assured.dto.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
@Getter
@ToString
public class HttpRequestDto {
    private String method;
    private String url;
    private Map<String, String> headers;
    private Map<String, String> cookies;
    private String body;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ОШИБКА ПРИ HTTP ОБМЕНЕ ===\n");
        sb.append("▶ ЗАПРОС: ").append(method).append(" ").append(url).append("\n");

        if (headers != null && !headers.isEmpty()) {
            sb.append("├─ Заголовки запроса:\n");
            headers.forEach((k, v) -> sb.append("│   ").append(k).append(": ").append(v).append("\n"));
        }

        if (cookies != null && !cookies.isEmpty()) {
            sb.append("├─ Cookies запроса:\n");
            cookies.forEach((k, v) -> sb.append("│   ").append(k).append(" = ").append(v).append("\n"));
        }

        if (body != null) {
            sb.append("└─ Тело запроса:\n");
            // body уже красиво отформатирован в фильтре, просто выводим с отступом
            sb.append(indentJson(body));
        }

        return sb.toString();
    }

    // Метод проверки строки JSON:
    private boolean isJson(String str) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readTree(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Помощник для отступов JSON
    private String indentJson(String json) {
        return Arrays.stream(json.split("\n"))
                .map(line -> "    " + line)
                .collect(Collectors.joining("\n")) + "\n";
    }
}