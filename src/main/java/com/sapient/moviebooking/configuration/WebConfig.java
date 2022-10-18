package com.sapient.moviebooking.configuration;

import com.sapient.moviebooking.convertor.IntToShowConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private IntToShowConverter intToShowConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(intToShowConverter);
    }
}
