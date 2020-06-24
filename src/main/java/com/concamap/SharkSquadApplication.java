package com.concamap;

import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.format.Formatter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

@SpringBootApplication
public class SharkSquadApplication implements WebMvcConfigurer {

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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        registry.addInterceptor(interceptor);
        interceptor.setParamName("lang");
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new SessionLocaleResolver();
    }
}
