package ru.yandex.praktikum.infrastructure.logback;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class PrettyMultilineLayout extends PatternLayout {
    private static final String INDENT = "         "; // 26 пробелов

    @Override
    public String doLayout(ILoggingEvent event) {
        String original = super.doLayout(event);
        return indentMultilineMessage(original);
    }

    private String indentMultilineMessage(String message) {
        // Добавляет отступ перед каждой строкой, кроме первой
        return message.replaceAll("(?m)(\\r?\\n)(?!$)", "$1" + INDENT);
    }
}
