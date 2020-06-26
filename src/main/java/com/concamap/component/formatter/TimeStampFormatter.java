package com.concamap.component.formatter;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class TimeStampFormatter implements Formatter<Timestamp> {

    @Override
    public Timestamp parse(String text, Locale locale) throws ParseException {
        return null;
    }

    @Override
    public String print(Timestamp timestamp, Locale locale) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        LocalDate localDate = localDateTime.toLocalDate();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", locale);
        return localDate.format(dateTimeFormatter);
    }
}
