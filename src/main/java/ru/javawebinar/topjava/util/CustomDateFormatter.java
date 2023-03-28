package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomDateFormatter implements Formatter<LocalDate> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate parse(String text, Locale locale) {
        return LocalDate.parse(text, DATE_TIME_FORMATTER);
    }

    @Override
    public String print(LocalDate localDate, Locale locale) {
        return localDate.toString();
    }
}
