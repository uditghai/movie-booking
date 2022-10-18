package com.sapient.moviebooking;

import com.sapient.moviebooking.dto.PriceRequest;
import com.sapient.moviebooking.dto.TheatreSeatRequest;
import com.sapient.moviebooking.entity.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sapient.moviebooking.dto.TheatreSeatRequest.of;
import static com.sapient.moviebooking.entity.City.Chandigarh;
import static com.sapient.moviebooking.entity.Language.English;
import static com.sapient.moviebooking.entity.SeatingClass.Standard;

public class TestBuilders {
    public static final int defaultNoOfTickets = 2;
    public static final long defaultCustomerId = 1;
    public static final double defaultPrice = 100;
    public static final int defaultMovieId = 17;
    public static final City defaultCity = Chandigarh;
    public static final int defaultTheatreId = 2;
    public static final int defaultShowId = 30;
    public static final String defaultReservationId = "08086255-562a";
    public static final TheatrePartner.Builder testTheatrePartnerBuilder =
            TheatrePartner.builder()
                    .id(defaultTheatreId)
                    .name("Test Partner")
                    .code("TEST");
    private static final int defaultMovieBookingId = 1;

    public static Movie.Builder testMovieBuilder() {
        return Movie.builder()
                .id(defaultMovieId)
                .name("Test Movie")
                .genres(List.of(Genre.Action))
                .language(English)
                .runTime(150);
    }

    public static Theatre.Builder testTheatreBuilder() {
        return Theatre.builder()
                .name("Test Theatre")
                .city(defaultCity)
                .ownedByTheatrePartner(testTheatrePartnerBuilder.build());
    }

    public static Show.Builder testShowBuilder() {
        return testShowBuilder(null, null);
    }

    public static Show.Builder testShowBuilder(Movie.Builder movieBuilder,
                                               Theatre.Builder theatreBuilder) {

        Movie movie = Optional.ofNullable(movieBuilder).orElse(testMovieBuilder()).build();
        Theatre theatre = Optional.ofNullable(theatreBuilder).orElse(testTheatreBuilder()).build();
        return Show.builder()
                .movie(movie)
                .showTime(LocalDateTime.now())
                .theatre(theatre)
                .status(ShowStatus.OpenForBooking);
    }

    public static PriceRequest.Builder testPriceRequestBuilder(Show.Builder showBuilder) {
        Show show = Optional.ofNullable(showBuilder).orElse(testShowBuilder()).build();
        return PriceRequest.builder()
                .show(show)
                .seatingClass(Standard)
                .customerId(defaultCustomerId);
    }

    public static PriceRequest.Builder testPriceRequestBuilder() {
        return testPriceRequestBuilder(null);
    }

    public static MovieBooking.Builder testMovieBookingBuilder(Show.Builder showBuilder) {
        Show show = Optional.ofNullable(showBuilder).orElse(testShowBuilder()).build();
        return MovieBooking.builder()
                .id(defaultMovieBookingId)
                .show(show)
                .bookingStatus(BookingStatus.PendingPayment)
                .noOfTickets(defaultNoOfTickets)
                .seatingClass(Standard)
                .customerId(defaultCustomerId)
                .price(defaultPrice);
    }

    public static MovieBooking.Builder testMovieBookingBuilder() {
        return testMovieBookingBuilder((Show.Builder) null);
    }

    public static MovieBooking.Builder testMovieBookingBuilder(PriceRequest.Builder priceRequestBuilder) {
        var priceRequest =
                Optional.ofNullable(priceRequestBuilder).orElse(testPriceRequestBuilder()).build();
        return MovieBooking.builder()
                .show(priceRequest.getShow())
                .noOfTickets(defaultNoOfTickets)
                .customerId(priceRequest.getCustomerId());
    }

    public static SeatReservation.Builder testReserveShowSeatRequestBuilder(List<TheatreSeatRequest> theatreSeatRequests) {
        return SeatReservation.builder()
                .showId(defaultShowId)
                .id(defaultReservationId)
                .createdDate(LocalDateTime.now())
                .theatreSeatRequests(theatreSeatRequests);
    }

    public static List<TheatreSeatRequest> testTheatreSeatRequestBuilder(String row, int totalSeatsToBook) {
        List<TheatreSeatRequest> theatreSeatRequests = new ArrayList<>();
        for (int seatNo = 1; seatNo <= totalSeatsToBook; seatNo++) {
            theatreSeatRequests.add(of(row, seatNo));
        }
        return theatreSeatRequests;
    }

    public static List<TheatreSeatRequest> testTheatreSeatRequestBuilder() {
        return testTheatreSeatRequestBuilder("A", 1);
    }
}
