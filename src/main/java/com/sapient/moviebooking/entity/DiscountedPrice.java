package com.sapient.moviebooking.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@RequiredArgsConstructor(staticName = "of")
public class DiscountedPrice {
    private final double totalPriceBeforeDiscount;
    private final double finalPrice;
    private final double pricePerTicket;
    private final int totalTickets;
    private final int discountedTickets;

    public static DiscountedPrice nonDiscountedPrice(final double pricePerTicket, final int totalTickets) {
        return DiscountedPrice.of(pricePerTicket * totalTickets,
                pricePerTicket * totalTickets,
                pricePerTicket,
                totalTickets,
                0);
    }
}
