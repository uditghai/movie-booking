package com.sapient.moviebooking;

import com.sapient.moviebooking.service.DiscountService;
import com.sapient.moviebooking.service.SimpleDiscountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.sapient.moviebooking.DiscountRules.initialiseRulesFor;

@Configuration
@Slf4j
public class DiscountServiceConfiguration {
    @Bean
    public DiscountService discountService() {
        DiscountService discountService = new SimpleDiscountService();
        initialiseRulesFor(discountService);
        log.info("Discounting rules added = {}",discountService.getRuleCount());
        return discountService;
    }
}
