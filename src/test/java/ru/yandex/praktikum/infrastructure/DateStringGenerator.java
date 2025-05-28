package ru.yandex.praktikum.infrastructure;

import java.time.LocalDate;

public class DateStringGenerator {
    private static LocalDate currentDate;

    public static String getCurrentDateString(){
        getCurrentDate();
        return currentDate.toString();
    }

    public static String getTomorrowDateString(){
        getCurrentDate();
        currentDate = currentDate.plusDays(1);
        return currentDate.toString();
    }

    private static void getCurrentDate(){
        currentDate = LocalDate.now();
    }
}
