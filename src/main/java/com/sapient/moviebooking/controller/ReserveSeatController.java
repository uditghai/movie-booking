package com.sapient.moviebooking.controller;

import com.sapient.moviebooking.entity.SeatReservation;
import com.sapient.moviebooking.service.ReserveSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reserve")
public class ReserveSeatController {
    @Autowired
    private final ReserveSeatService reserveSeatService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public SeatReservation requestSeatReservation(@Valid @RequestBody SeatReservation reservedSeatRequest) {
        return reserveSeatService.reserve(reservedSeatRequest);
    }

    @GetMapping(value = "/{reservationId}")
    public SeatReservation getSeatReservation(@PathVariable @NotNull String reservationId) {
        return reserveSeatService.getReservation(reservationId);
    }

    @DeleteMapping(value = "/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelSeatReservation(@PathVariable @NotNull String reservationId) {
        reserveSeatService.cancel(reservationId);
    }

    @PostMapping(value = "/{reservationId}/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmSeatReservation(@PathVariable @NotNull String reservationId) {
        reserveSeatService.confirm(reservationId);
    }
}
