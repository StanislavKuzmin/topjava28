package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomTimeFormatter implements Formatter<LocalTime> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public LocalTime parse(String text, Locale locale) {
        return LocalTime.parse(text, DATE_TIME_FORMATTER);
    }

    @Override
    public String print(LocalTime localTime, Locale locale) {
        return localTime.toString();
    }
}
