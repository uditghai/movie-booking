package com.sapient.moviebooking.service;

import com.sapient.moviebooking.entity.Discount;
import com.sapient.moviebooking.entity.MovieBooking;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class SimpleDiscountService implements DiscountService {
    private final Map<Predicate<MovieBooking>, Discount> configuredRules = new HashMap<>();

    @Override
    public void addRule(Predicate<MovieBooking> matchRule, Discount discount) {
        configuredRules.put(matchRule, discount);
    }

    @Override
    public int getRuleCount() {
        return configuredRules.size();
    }

    @Override
    public void clearRules() {
        configuredRules.clear();
    }

    @Override
    public Optional<Discount> getApplicableDiscounts(MovieBooking movieBooking) {
        return configuredRules.entrySet().stream()
                .filter(entry -> entry.getKey().test(movieBooking))
                .map(Map.Entry::getValue)
                .findFirst();
    }
}
