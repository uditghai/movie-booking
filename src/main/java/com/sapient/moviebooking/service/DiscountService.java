package com.sapient.moviebooking.service;

import com.sapient.moviebooking.entity.Discount;
import com.sapient.moviebooking.entity.DiscountedPrice;
import com.sapient.moviebooking.entity.MovieBooking;

import java.util.Optional;
import java.util.function.Predicate;

public interface DiscountService {
    void addRule(Predicate<MovieBooking> matchRule, Discount discount);

    int getRuleCount();

    void clearRules();

    Optional<Discount> getApplicableDiscounts(MovieBooking movieBooking);

    default DiscountedPrice calculateDiscountedPrice(MovieBooking movieBooking, double pricePerTicket) {
        var applicableDiscount = getApplicableDiscounts(movieBooking);
        return applicableDiscount.map(discount -> {
            int totalTicketsToDiscount =
                    Math.min(discount.getMaxTicketsToDiscount().orElse(Integer.MAX_VALUE),
                            Math.max(0, movieBooking.getNoOfTickets() -
                                    discount.getDiscountedTicketFrom().orElse(1) + 1));
            var discountedPrice = discount.getDiscountPercentage().map(discountPercentage ->
                            totalTicketsToDiscount * getMultiplier(discountPercentage) * pricePerTicket)
                    .orElse(totalTicketsToDiscount * discount.getFlatRate().orElse(pricePerTicket));
            var remainingPrice =
                    (movieBooking.getNoOfTickets() - totalTicketsToDiscount) * pricePerTicket;
            var finalPrice = discountedPrice + remainingPrice;
            return DiscountedPrice.of(pricePerTicket * movieBooking.getNoOfTickets(),
                    finalPrice, pricePerTicket, movieBooking.getNoOfTickets(), totalTicketsToDiscount);
        }).orElse(DiscountedPrice.nonDiscountedPrice(pricePerTicket, movieBooking.getNoOfTickets()));
    }

    private double getMultiplier(Double discountPercentage) {
        return (100d - Math.min(discountPercentage, 100d)) / 100d;
    }
}