package com.sapient.moviebooking;

import com.sapient.moviebooking.service.CityBasedPriceService;
import com.sapient.moviebooking.service.PriceService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.sapient.moviebooking.TestBuilders.*;
import static com.sapient.moviebooking.dto.PriceRequest.of;
import static com.sapient.moviebooking.entity.City.NewDelhi;
import static com.sapient.moviebooking.entity.SeatingClass.*;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(MockitoExtension.class)
class PriceServiceTests {

    private final PriceService priceService = new CityBasedPriceService();

    private final double defaultPrice = 100d;

    @Test
    void shouldFetchStandardPrice() {
        var request = of(1, testShowBuilder().build(), Standard);
        var singleTicketPrice = priceService.getTicketPrice(request);

        assertThat(singleTicketPrice, Matchers.equalTo(defaultPrice));
    }

    @Test
    void shouldFetchSpecialPriceBasedSeating() {
        var request = of(1, testShowBuilder().build(), Executive);
        var singleTicketPrice = priceService.getTicketPrice(request);

        assertThat(singleTicketPrice, Matchers.equalTo(defaultPrice * 1.2));
    }

    @Test
    void shouldFetchSpecialPriceBasedCity() {
        var theatreBuilder = testTheatreBuilder().city(NewDelhi);

        var request = of(1, testShowBuilder(testMovieBuilder(), theatreBuilder).build(), Standard);
        var singleTicketPrice = priceService.getTicketPrice(request);

        assertThat(singleTicketPrice, Matchers.equalTo(defaultPrice * 1.2));
    }

    @Test
    void shouldFetchSpecialPriceBasedCityAndSeating() {
        var theatreBuilder = testTheatreBuilder().city(NewDelhi);

        var request = of(1, testShowBuilder(testMovieBuilder(), theatreBuilder).build(), Recliner);
        var singleTicketPrice = priceService.getTicketPrice(request);

        assertThat(singleTicketPrice, Matchers.equalTo(defaultPrice * 1.2 * 1.5));
    }
}
