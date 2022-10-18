package com.sapient.moviebooking;

import com.sapient.moviebooking.entity.ReservedShowSeat;
import com.sapient.moviebooking.entity.SeatReservation;
import com.sapient.moviebooking.repository.ShowRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@NoArgsConstructor
@Service

public class SeatReservationValidator {
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Getter
    private Predicate<SeatReservation> hasValidTheatreSeats =
            reservedSeat -> {
                var show = showRepository.findById(reservedSeat.getShowId()).orElseThrow();
                // seats specified are present in theatre
                var theatreSeats =
                        show.getTheatre().getTheatreSeats().stream().filter(
                                theatreSeat -> reservedSeat
                                        .getTheatreSeatRequests().stream()
                                        .anyMatch(reqSeat -> theatreSeat.getSeatNo() == reqSeat.getSeatNo()
                                                && theatreSeat.getRow().equalsIgnoreCase(reqSeat.getRow()))
                        ).toList();
                return theatreSeats.size() == reservedSeat.getTheatreSeatRequests().size();
            };

    @Getter
    private Predicate<SeatReservation> hasAllSeatsStillReserved = seatReservation -> {
        var reservedSeats =
                redisTemplate.opsForValue().multiGet(ReservedShowSeat.of(seatReservation).stream()
                        .map(ReservedShowSeat::getId).toList());

        return reservedSeats != null && reservedSeats
                .stream().filter(id -> id.equalsIgnoreCase(seatReservation.getId()))
                .count() == seatReservation.getTheatreSeatRequests().size();
    };

}
