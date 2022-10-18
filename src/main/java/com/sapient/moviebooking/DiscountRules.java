package com.sapient.moviebooking;

import com.sapient.moviebooking.entity.City;
import com.sapient.moviebooking.entity.Discount;
import com.sapient.moviebooking.entity.MovieBooking;
import com.sapient.moviebooking.service.DiscountService;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;

import static com.sapient.moviebooking.entity.City.Chandigarh;
import static com.sapient.moviebooking.entity.City.NewDelhi;
import static java.time.LocalTime.NOON;
import static java.util.Optional.of;

public class DiscountRules {
    private static final List<City> discountedCities = List.of(Chandigarh, NewDelhi);

    private DiscountRules() {
    }

    public static void initialiseRulesFor(@NotNull DiscountService discountService) {
        discountService.addRule(Rules.afternoonTicketsInSpecificCities,
                Discounts.standard20PercentDiscount);
        discountService.addRule(Rules.threeOrMoreTickets, Discounts.thirdTicket20PercentDiscount);
    }

    static class Rules {
        private static final Predicate<MovieBooking> specificCities =
                (MovieBooking movieBooking) -> discountedCities.contains(movieBooking.getShow().getTheatre().getCity());
        private static final Predicate<MovieBooking> afternoonTickets =
                (MovieBooking movieBooking) -> {
                    var currentShowTime = movieBooking.getShow().getShowTime().toLocalTime();
                    return currentShowTime.isAfter(NOON)
                            && currentShowTime.isBefore(LocalTime.of(18, 0));
                };
        private static final Predicate<MovieBooking> afternoonTicketsInSpecificCities =
                afternoonTickets.and(specificCities);
        private static final Predicate<MovieBooking> threeOrMoreTickets =
                (MovieBooking movieBooking) -> movieBooking.getNoOfTickets() >= 3;

        private Rules() {
        }
    }

    static class Discounts {
        private static final Discount standard20PercentDiscount =
                Discount.builder().discountPercentage(of(20d)).build();
        private static final Discount thirdTicket20PercentDiscount =
                Discount.builder()
                        .discountPercentage(of(20d))
                        .discountedTicketFrom(of(3))
                        .maxTicketsToDiscount(of(1))
                        .build();
        private Discounts() {
        }
    }
}
