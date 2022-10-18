package com.sapient.moviebooking;

import com.sapient.moviebooking.dto.TheatreSeatRequest;
import com.sapient.moviebooking.entity.SeatReservation;
import com.sapient.moviebooking.repository.ShowRepository;
import com.sapient.moviebooking.service.ReserveSeatService;
import lombok.NoArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.UUID;

import static com.sapient.moviebooking.TestBuilders.defaultShowId;
import static com.sapient.moviebooking.TestBuilders.testReserveShowSeatRequestBuilder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@NoArgsConstructor
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class SeatReservationTest {
    @Autowired
    private ReserveSeatService reserveSeatService;

    @Autowired
    private ShowRepository showRepository;
    private SeatReservation seatReservation;

    @Test
    void shouldReserveSeat() {
        var sessionId = UUID.randomUUID().toString();
        var show = showRepository.findById(defaultShowId).orElseThrow();
        var seatsToBook = show.getTheatre().getTheatreSeats().stream().limit(2).map(TheatreSeatRequest::of).toList();

        seatReservation = testReserveShowSeatRequestBuilder(seatsToBook).id(sessionId).build();
        reserveSeatService.reserve(seatReservation);

        var getSeatReservation = reserveSeatService.getReservation(seatReservation.getId());
        compareSeatReservation(getSeatReservation, seatReservation);

        reserveSeatService.extendReservation(seatReservation.getId(), 900);
        getSeatReservation = reserveSeatService.getReservation(seatReservation.getId());
        compareSeatReservation(getSeatReservation, seatReservation);

        reserveSeatService.confirm(seatReservation.getId());
        getSeatReservation = reserveSeatService.getReservation(seatReservation.getId());

        compareSeatReservation(getSeatReservation, seatReservation);

        reserveSeatService.cancel(seatReservation.getId());

        assertThrows(NoSuchElementException.class,
                () -> reserveSeatService.getReservation(seatReservation.getId()));
    }

    private void compareSeatReservation(SeatReservation actual, SeatReservation expected) {
        assertThat(actual.getId(), Matchers.equalTo(expected.getId()));
        assertThat(actual.getCreatedDate(), Matchers.equalTo(expected.getCreatedDate()));
        assertThat(actual.getTheatreSeatRequests(), Matchers.equalTo(expected.getTheatreSeatRequests()));
    }
}
