package com.sapient.moviebooking.service;

import com.sapient.moviebooking.entity.SeatReservation;

import java.time.Duration;

public interface ReserveSeatService {

    SeatReservation reserve(SeatReservation seatReservation);

    SeatReservation reserve(SeatReservation reservedSeatRequest,
                 Duration reservationDuration);

    SeatReservation getReservation(String reservationId);

    void confirm(String reservationId);

    void extendReservation(String reservationId, int timeToLive);

    void cancel(String reservationId);
    //void cancel(String reservationId, List<TheatreSeatRequest> theatreSeatRequests);
}
