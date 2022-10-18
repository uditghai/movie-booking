package com.sapient.moviebooking.service;

import com.sapient.moviebooking.ConcurrentReservationException;
import com.sapient.moviebooking.SeatNoLongerReservedException;
import com.sapient.moviebooking.SeatReservationValidator;
import com.sapient.moviebooking.entity.ReservedShowSeat;
import com.sapient.moviebooking.entity.SeatReservation;
import com.sapient.moviebooking.repository.SeatReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.Duration;

import static com.sapient.moviebooking.entity.SeatReservation.of;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class RedisReservationService implements ReserveSeatService {

    private static final int DEFAULT_TIME_TO_LIVE = 300;
    @Autowired
    private final SeatReservationRepository seatReservationRepository;

    @Autowired
    private final SeatReservationValidator seatReservationValidator;
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public SeatReservation reserve(SeatReservation reservedSeatRequest) {
        return reserve(reservedSeatRequest, Duration.ofSeconds(DEFAULT_TIME_TO_LIVE));
    }

    @Override
    public SeatReservation reserve(@Valid SeatReservation reservedSeatRequest, Duration reservationDuration) {
        int timeToLive = (int) reservationDuration.getSeconds();
        log.info("reserveRequest={}", reservedSeatRequest.toString());

        seatReservationValidator.getHasValidTheatreSeats().test(reservedSeatRequest);

        var seatsToSave = ReservedShowSeat.of(reservedSeatRequest, timeToLive);

        SeatReservation savedReservation =
                seatReservationRepository.save(of(reservedSeatRequest));

        // Save and check if any seat is not reserved
        var seatsNotReserved =
                seatsToSave.stream().dropWhile(
                                seat -> Boolean.TRUE.equals(redisTemplate.execute(saveIfNotExist(seat, timeToLive))))
                        .toList();

        if (!seatsNotReserved.isEmpty()) {
            // if any seat is not saved, remove locks on other seats
            log.warn("ReservationId: - seatsNotSaved ={}", seatsNotReserved);
            seatsToSave.stream().filter(seat -> !seatsNotReserved.contains(seat))
                    .forEach(seat -> redisTemplate.delete(seat.getId()));

            throw new ConcurrentReservationException("Specified seat(s) is already reserved %s".formatted(seatsNotReserved));
        }
        return savedReservation;
    }

    @Override
    @Transactional(readOnly = true)
    public SeatReservation getReservation(@NotBlank String reservationId) {
        return seatReservationRepository.findById(reservationId).orElseThrow();
    }

    @Override
    public void confirm(@NotBlank String reservationId) {
        SeatReservation existingReservation =
                seatReservationRepository.findById(reservationId).orElseThrow();
        if (!seatReservationValidator.getHasValidTheatreSeats().test(existingReservation)) {
            throw new SeatNoLongerReservedException();
        }
        ReservedShowSeat.of(existingReservation).forEach(
                seat -> redisTemplate.execute(persist(seat)));
    }

    @Override
    public void extendReservation(@NotBlank String reservationId, int timeToLive) {
        SeatReservation existingReservation =
                seatReservationRepository.findById(reservationId).orElseThrow();

        if (!seatReservationValidator.getHasValidTheatreSeats().test(existingReservation)) {
            throw new SeatNoLongerReservedException();
        }

        ReservedShowSeat.of(existingReservation, timeToLive).forEach(
                seat -> redisTemplate.execute(setTtl(seat, timeToLive)));
    }


    @Override
    public void cancel(String reservationId) {
        SeatReservation existingReservation =
                seatReservationRepository.findById(reservationId).orElseThrow();

        var removedSeats = redisTemplate.delete(ReservedShowSeat.of(existingReservation).stream()
                .map(ReservedShowSeat::getId).toList());

        log.debug("Total seats cancelled = {}", removedSeats);

        seatReservationRepository.delete(existingReservation);
    }

    private RedisCallback<Boolean> persist(
            ReservedShowSeat reservedShowSeat) {
        byte[] rawKey = reservedShowSeat.getId().getBytes();
        return connection -> connection.persist(rawKey);
    }

    private RedisCallback<Boolean> setTtl(
            ReservedShowSeat reservedShowSeat,
            int timeToLive) {
        byte[] rawKey = reservedShowSeat.getId().getBytes();
        return connection -> connection.expire(rawKey, timeToLive);
    }

    private RedisCallback<Boolean> saveIfNotExist(
            ReservedShowSeat reservedShowSeat,
            int timeToLive) {
        byte[] rawKey = reservedShowSeat.getId().getBytes();
        byte[] rawValue = reservedShowSeat.getReservationId().getBytes();
        return connection -> Boolean.TRUE.equals(connection.setNX(rawKey, rawValue)) &&
                Boolean.TRUE.equals(connection.expire(rawKey, timeToLive));
    }
}