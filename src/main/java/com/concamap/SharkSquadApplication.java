package com.concamap;

import org.apache.commons.text.WordUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

@SpringBootApplication
public class SharkSquadApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharkSquadApplication.class, args);
    }

    @Bean
    public Formatter<Month> monthFormatter() {
        return new Formatter<Month>() {
            @Override
            public Month parse(String text, Locale locale) {
                return null;
            }

            @Override
            public String print(Month month, Locale locale) {
                String monthDisplay = month.getDisplayName(TextStyle.FULL, locale);
                return WordUtils.capitalizeFully(monthDisplay);
            }
        };
    }
}
