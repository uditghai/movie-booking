package com.sapient.moviebooking.service;

import com.sapient.moviebooking.dto.MovieBookingRequest;
import com.sapient.moviebooking.dto.PriceRequest;
import com.sapient.moviebooking.dto.TheatreSeatRequest;
import com.sapient.moviebooking.entity.DiscountedPrice;
import com.sapient.moviebooking.entity.MovieBooking;
import com.sapient.moviebooking.entity.Show;
import com.sapient.moviebooking.entity.TheatreSeat;
import com.sapient.moviebooking.repository.MovieBookingRepository;
import com.sapient.moviebooking.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiPredicate;

import static com.sapient.moviebooking.entity.BookingStatus.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SimpleMovieBookingService implements MovieBookingService {
    @Autowired
    private final MovieBookingRepository movieBookingRepository;
    @Autowired
    private final PriceService priceService;
    @Autowired
    private final ShowRepository showRepository;

    @Autowired
    private final ReserveSeatService reserveSeatService;

    @Autowired
    private final DiscountService discountService;
    private final BiPredicate<TheatreSeatRequest, TheatreSeat> areTheatreSeatAndRequestEqual =
            (TheatreSeatRequest theatreSeatRequest,
             TheatreSeat theatreSeat) -> theatreSeatRequest.getRow().equalsIgnoreCase(theatreSeat.getRow())
                    && theatreSeatRequest.getSeatNo() == theatreSeat.getSeatNo();

    @Override
    public MovieBooking bookMovie(MovieBookingRequest movieBookingRequest) {
        final int defaultWaitTimeForPayment = 300;

        Show show = showRepository.findById(movieBookingRequest.getShowId()).orElseThrow();

        double pricePerTicket = priceService.getTicketPrice(
                PriceRequest.of(movieBookingRequest.getCustomerId(), show,
                        movieBookingRequest.getSeatingClass()));
        MovieBooking booking = getMovieBookingBuilder(movieBookingRequest, show).build();
        DiscountedPrice discountedPrice =
                discountService.calculateDiscountedPrice(booking, pricePerTicket);
        log.info("discounted price for request {} = {}",booking.getSessionId(),discountedPrice);
        reserveSeatService.extendReservation(movieBookingRequest.getSessionId(), defaultWaitTimeForPayment);
        return movieBookingRepository.save(booking.toBuilder().price(discountedPrice.getFinalPrice()).build());
    }

    @Override
    public MovieBooking getMovieBookingById(int movieId) {
        return movieBookingRepository.findById(movieId).orElseThrow();
    }

    @Override
    public MovieBooking cancelMovieBooking(int movieBookingId) {
        MovieBooking existingBooking = getMovieBookingById(movieBookingId);
        reserveSeatService.cancel(existingBooking.getSessionId());
        return movieBookingRepository.save(existingBooking.toBuilder().bookingStatus(Cancelled).build());
    }

    @Override
    public MovieBooking confirmMovieBooking(int movieId) {
        MovieBooking bookedMovie = getMovieBookingById(movieId).toBuilder().bookingStatus(Booked).build();
        reserveSeatService.confirm(bookedMovie.getSessionId());
        return movieBookingRepository.save(bookedMovie);
    }

    private MovieBooking.Builder getMovieBookingBuilder(MovieBookingRequest movieBookingRequest,
                                                        Show show) {
        List<TheatreSeat> theatreSeats = show.getTheatre().getTheatreSeats().stream().filter(
                theatreSeat -> movieBookingRequest.getTheatreSeatRequests().stream().anyMatch(
                        theatreSeatRequest -> areTheatreSeatAndRequestEqual.test(theatreSeatRequest, theatreSeat))).toList();
        return MovieBooking.builder()
                .customerId(movieBookingRequest.getCustomerId())
                .seatingClass(movieBookingRequest.getSeatingClass())
                .bookingStatus(PendingPayment)
                .theatreSeats(theatreSeats)
                .noOfTickets(theatreSeats.size())
                .sessionId(movieBookingRequest.getSessionId())
                .createdDate(LocalDateTime.now())
                .show(show);
    }
}
