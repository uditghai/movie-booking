package com.sapient.moviebooking;

import com.sapient.moviebooking.dto.MovieBookingRequest;
import com.sapient.moviebooking.dto.TheatreSeatRequest;
import com.sapient.moviebooking.entity.BookingStatus;
import com.sapient.moviebooking.entity.MovieBooking;
import com.sapient.moviebooking.entity.SeatReservation;
import com.sapient.moviebooking.repository.MovieBookingRepository;
import com.sapient.moviebooking.repository.SeatReservationRepository;
import com.sapient.moviebooking.repository.ShowRepository;
import com.sapient.moviebooking.service.MovieBookingService;
import com.sapient.moviebooking.service.ReserveSeatService;
import lombok.NoArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.sapient.moviebooking.TestBuilders.*;
import static com.sapient.moviebooking.entity.SeatingClass.Standard;
import static java.util.Objects.isNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@NoArgsConstructor
@ExtendWith(MockitoExtension.class)
class MovieBookingFunctionalTests {

    @Autowired
    private MovieBookingService movieBookingService;
    @Autowired
    private MovieBookingRepository movieBookingRepository;
    @Autowired
    private ReserveSeatService reserveSeatService;
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private SeatReservationRepository seatReservationRepository;
    private SeatReservation seatReservation;
    private MovieBooking createdBooking;

    @AfterEach
    void tearDown() {
        if (!isNull(seatReservation)) {
            reserveSeatService.cancel(seatReservation.getId());
            seatReservationRepository.delete(seatReservation);
        }
        if (!isNull(createdBooking)) {
            movieBookingRepository.delete(createdBooking);
        }
    }

    @Test
    void shouldBookMovieTicket() {

        var sessionId = UUID.randomUUID().toString();
        var show = showRepository.findById(defaultShowId).orElseThrow();
        var seatsToBook = show.getTheatre().getTheatreSeats().stream().limit(2).map(TheatreSeatRequest::of).toList();

        seatReservation = testReserveShowSeatRequestBuilder(seatsToBook).id(sessionId).build();

        reserveSeatService.reserve(seatReservation);
        MovieBookingRequest bookingRequest = MovieBookingRequest.of(seatReservation).toBuilder().customerId(defaultCustomerId).seatingClass(Standard).build();

        createdBooking = movieBookingService.bookMovie(bookingRequest);

        assertThat(createdBooking.getNoOfTickets(), Matchers.equalTo(bookingRequest.getTheatreSeatRequests().size()));
        assertThat(createdBooking.getCustomerId(), Matchers.equalTo(bookingRequest.getCustomerId()));

        assertThat(createdBooking.getBookingStatus(), Matchers.equalTo(BookingStatus.PendingPayment));

        assertThat(createdBooking.getSeatingClass(), Matchers.equalTo(bookingRequest.getSeatingClass()));

        Double expectedPrice = 200d;
        assertThat(createdBooking.getPrice(), Matchers.equalTo(expectedPrice));

        //verify(movieBookingRepository, times(1)).save(any());
    }

    @Test
    void shouldBookMovieTicketWithThirdTicketDiscount() {

        var sessionId = UUID.randomUUID().toString();
        var show = showRepository.findById(defaultShowId).orElseThrow();
        var seatsToBook = show.getTheatre().getTheatreSeats().stream().limit(4).map(TheatreSeatRequest::of).toList();

        seatReservation = testReserveShowSeatRequestBuilder(seatsToBook).id(sessionId).build();

        reserveSeatService.reserve(seatReservation);
        MovieBookingRequest bookingRequest = MovieBookingRequest.of(seatReservation).toBuilder().customerId(defaultCustomerId).seatingClass(Standard).build();

        createdBooking = movieBookingService.bookMovie(bookingRequest);

        assertThat(createdBooking.getNoOfTickets(), Matchers.equalTo(bookingRequest.getTheatreSeatRequests().size()));
        assertThat(createdBooking.getCustomerId(), Matchers.equalTo(bookingRequest.getCustomerId()));

        assertThat(createdBooking.getBookingStatus(), Matchers.equalTo(BookingStatus.PendingPayment));

        assertThat(createdBooking.getSeatingClass(), Matchers.equalTo(bookingRequest.getSeatingClass()));

        Double expectedPrice = 100d * 3 + 100 * 0.8;
        assertThat(createdBooking.getPrice(), Matchers.equalTo(expectedPrice));

    }

    @Test
    void shouldNotAllowParallelBookingOfSameSeat() {
        var sessionIdA = UUID.randomUUID().toString();
        var sessionIdB = UUID.randomUUID().toString();
        var totalNoOfTickets = 2;
        var show = showRepository.findById(defaultShowId).orElseThrow();
        var seatsToBook = show.getTheatre().getTheatreSeats().stream().limit(totalNoOfTickets).map(TheatreSeatRequest::of).toList();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        var seatReservationSessionA = executor.submit(getCallableReservation(seatsToBook, sessionIdA));
        var seatReservationSessionB = executor.submit(getCallableReservation(seatsToBook, sessionIdB));


        assertThrows(ExecutionException.class, () -> {
            seatReservationSessionA.get();
            seatReservationSessionB.get();
        });
    }

    private Callable<SeatReservation> getCallableReservation(List<TheatreSeatRequest> theatreSeatRequests, String sessionId) {
        return () -> {
            SeatReservation seatReservation = testReserveShowSeatRequestBuilder(theatreSeatRequests).id(sessionId).build();
            reserveSeatService.reserve(seatReservation);
            reserveSeatService.cancel(seatReservation.getId());
            return seatReservation;
        };
    }
}
