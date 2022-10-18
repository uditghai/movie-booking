package com.sapient.moviebooking;

import com.sapient.moviebooking.service.DiscountService;
import com.sapient.moviebooking.service.PriceService;
import com.sapient.moviebooking.service.SimpleDiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static com.sapient.moviebooking.DiscountRules.initialiseRulesFor;
import static com.sapient.moviebooking.TestBuilders.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTests {
    private final DiscountService discountService = new SimpleDiscountService();
    @Mock
    private PriceService priceService;

    @BeforeEach()
    void setUp() {
        discountService.clearRules();
        initialiseRulesFor(discountService);
        when(priceService.getTicketPrice(any())).thenReturn(100d);
    }

    @Test
    void shouldNotDiscountRegularShows() {
        var priceRequest =
                testPriceRequestBuilder(testShowBuilder()
                        .showTime(LocalDate.now().atTime(11, 0)))
                        .build();

        var pricePerTicket = priceService.getTicketPrice(priceRequest);

        var movieBooking = testMovieBookingBuilder(priceRequest.toBuilder()).build();

        var discountedPrice = discountService.calculateDiscountedPrice(movieBooking, pricePerTicket);
        var expectedOriginalPrice = movieBooking.getNoOfTickets() * pricePerTicket;
        assertThat(discountService.getRuleCount(), greaterThanOrEqualTo(1));
        assertThat(discountedPrice, notNullValue());
        assertThat(discountedPrice.getPricePerTicket(), equalTo(pricePerTicket));
        assertThat(discountedPrice.getTotalTickets(), equalTo(movieBooking.getNoOfTickets()));
        assertThat(discountedPrice.getTotalPriceBeforeDiscount(), equalTo(expectedOriginalPrice));

        assertThat(discountedPrice.getFinalPrice(), equalTo(expectedOriginalPrice));
    }

    @Test
    void shouldNotDiscountShowsWithoutRules() {
        discountService.clearRules();
        var priceRequest =
                testPriceRequestBuilder(testShowBuilder()
                        .showTime(LocalDate.now().atTime(11, 0)))
                        .build();

        var pricePerTicket = priceService.getTicketPrice(priceRequest);

        var movieBooking = testMovieBookingBuilder(priceRequest.toBuilder()).build();

        var discountedPrice = discountService.calculateDiscountedPrice(movieBooking, pricePerTicket);
        var expectedOriginalPrice = movieBooking.getNoOfTickets() * pricePerTicket;
        assertThat(discountService.getRuleCount(), equalTo(0));
        assertThat(discountedPrice, notNullValue());
        assertThat(discountedPrice.getPricePerTicket(), equalTo(pricePerTicket));
        assertThat(discountedPrice.getTotalTickets(), equalTo(movieBooking.getNoOfTickets()));
        assertThat(discountedPrice.getTotalPriceBeforeDiscount(), equalTo(expectedOriginalPrice));

        assertThat(discountedPrice.getFinalPrice(), equalTo(expectedOriginalPrice));
    }

    @Test
    void shouldDiscountAfternoonShows() {
        var priceRequest =
                testPriceRequestBuilder(testShowBuilder()
                        .showTime(LocalDate.now().atTime(13, 0)))
                        .build();
        var pricePerTicket = priceService.getTicketPrice(priceRequest);

        var movieBooking = testMovieBookingBuilder(priceRequest.toBuilder()).price(pricePerTicket).build();

        var discountedPrice = discountService.calculateDiscountedPrice(movieBooking, pricePerTicket);

        var expectedOriginalPrice = movieBooking.getNoOfTickets() * pricePerTicket;
        assertThat(discountedPrice, notNullValue());
        assertThat(discountedPrice.getPricePerTicket(), equalTo(pricePerTicket));
        assertThat(discountedPrice.getTotalTickets(), equalTo(movieBooking.getNoOfTickets()));
        assertThat(discountedPrice.getTotalPriceBeforeDiscount(), equalTo(expectedOriginalPrice));

        assertThat(discountedPrice.getFinalPrice(), equalTo(expectedOriginalPrice * 0.8));
    }

    @Test
    void shouldDiscountExactlyThreeTickets() {
        var priceRequest =
                testPriceRequestBuilder(testShowBuilder()
                        .showTime(LocalDate.now().atTime(8, 0)))
                        .build();
        var pricePerTicket = priceService.getTicketPrice(priceRequest);

        var movieBooking = testMovieBookingBuilder(priceRequest.toBuilder())
                .price(pricePerTicket)
                .noOfTickets(3)
                .build();

        var discountedPrice = discountService.calculateDiscountedPrice(movieBooking, pricePerTicket);

        var expectedPrice = (pricePerTicket * 2) + (pricePerTicket * 0.8);
        var expectedOriginalPrice = movieBooking.getNoOfTickets() * pricePerTicket;
        assertThat(discountedPrice, notNullValue());
        assertThat(discountedPrice.getPricePerTicket(), equalTo(pricePerTicket));
        assertThat(discountedPrice.getTotalTickets(), equalTo(movieBooking.getNoOfTickets()));
        assertThat(discountedPrice.getTotalPriceBeforeDiscount(), equalTo(expectedOriginalPrice));

        assertThat(discountedPrice.getFinalPrice(), equalTo(expectedPrice));
    }
    @Test
    void shouldDiscountThreeOrMoreTickets() {
        var priceRequest =
                testPriceRequestBuilder(testShowBuilder()
                        .showTime(LocalDate.now().atTime(8, 0)))
                        .build();
        var pricePerTicket = priceService.getTicketPrice(priceRequest);

        var movieBooking = testMovieBookingBuilder(priceRequest.toBuilder()).price(pricePerTicket)
                .noOfTickets(5).build();

        var discountedPrice = discountService.calculateDiscountedPrice(movieBooking, pricePerTicket);

        var expectedPrice = (pricePerTicket * 4) + (pricePerTicket * 0.8);
        var expectedOriginalPrice = movieBooking.getNoOfTickets() * pricePerTicket;
        assertThat(discountedPrice, notNullValue());
        assertThat(discountedPrice.getPricePerTicket(), equalTo(pricePerTicket));
        assertThat(discountedPrice.getTotalTickets(), equalTo(movieBooking.getNoOfTickets()));
        assertThat(discountedPrice.getTotalPriceBeforeDiscount(), equalTo(expectedOriginalPrice));

        assertThat(discountedPrice.getFinalPrice(), equalTo(expectedPrice));
    }
}