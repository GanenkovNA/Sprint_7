package ru.yandex.praktikum.infrastructure.rest_assured.dto.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
public class HttpResponseDto {
    private int statusCode;
    private String statusText;     // Например: "Bad Request"
    private long responseTimeMs;   // Например: 245
    private Map<String, String> headers;
    private Object body;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("▶ Ответ: HTTP/1.1 ").append(statusCode)
                .append(" ").append(statusText)
                .append(" (").append(responseTimeMs).append(" мс)\n");

        // Заголовки
        if (headers != null && !headers.isEmpty()) {
            sb.append("├─ Заголовки ответа:\n");
            headers.forEach((k, v) -> sb.append("│   ").append(k).append(": ").append(v).append("\n"));
        }

        // Тело
        if (body != null) {
            sb.append("└─ Тело ответа:\n");
            try {
                String json = new ObjectMapper()
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(body);
                sb.append(indentJson(json));
            } catch (JsonProcessingException e) {
                sb.append("  (Ошибка сериализации тела)\n");
            }
        }

        sb.append("=== КОНЕЦ HTTP ОБМЕНА ===\n");

        return sb.toString();
    }

    private String indentJson(String json) {
        return Arrays.stream(json.split("\n"))
                .map(line -> "    " + line)
                .collect(Collectors.joining("\n")) + "\n";
    }
}
