package com.sapient.moviebooking.controller;

import com.sapient.moviebooking.dto.MovieBookingRequest;
import com.sapient.moviebooking.entity.MovieBooking;
import com.sapient.moviebooking.service.MovieBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/moviebooking")
public class MovieBookingController {

    private final MovieBookingService movieBookingService;

    @PostMapping
    @ResponseStatus(CREATED)
    private MovieBooking bookMovie(@RequestBody @Valid MovieBookingRequest movieBookingRequest) {
        return movieBookingService.bookMovie(movieBookingRequest);
    }

    @GetMapping("/{movieBookingId}")
    @ResponseStatus(OK)
    private MovieBooking getBooking(@PathVariable int movieBookingId) {
        return movieBookingService.getMovieBookingById(movieBookingId);
    }
    @PostMapping("/{movieBookingId}/confirm")
    @ResponseStatus(OK)
    private MovieBooking confirmBooking(@PathVariable int movieBookingId) {
        return movieBookingService.confirmMovieBooking(movieBookingId);
    }
    @DeleteMapping("/{movieBookingId}")
    @ResponseStatus(OK)
    private MovieBooking deleteBooking(@PathVariable int movieBookingId) {
        return movieBookingService.cancelMovieBooking(movieBookingId);
    }
}
