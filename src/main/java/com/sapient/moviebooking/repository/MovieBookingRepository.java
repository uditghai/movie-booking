package com.sapient.moviebooking.repository;

import com.sapient.moviebooking.entity.MovieBooking;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MovieBookingRepository extends PagingAndSortingRepository<MovieBooking, Integer> {
}
