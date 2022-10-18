package com.sapient.moviebooking.service;

import com.sapient.moviebooking.dto.MovieBookingRequest;
import com.sapient.moviebooking.entity.MovieBooking;

public interface MovieBookingService {
    MovieBooking bookMovie(MovieBookingRequest movieBookingRequest);
    MovieBooking getMovieBookingById(int movieId);

    MovieBooking cancelMovieBooking(int movieId);

    MovieBooking confirmMovieBooking(int movieId);
}
