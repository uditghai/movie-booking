package com.sapient.moviebooking.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
@Builder(toBuilder = true, builderClassName = "Builder")
public class Discount {
    @lombok.Builder.Default
    private final Optional<Integer> discountedTicketFrom = Optional.empty();
    @lombok.Builder.Default
    private final Optional<Integer> maxTicketsToDiscount = Optional.empty();
    @lombok.Builder.Default
    private final Optional<Double> discountPercentage = Optional.empty();
    @lombok.Builder.Default
    private final Optional<Double> flatRate = Optional.empty();
}
