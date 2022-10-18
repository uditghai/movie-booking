package com.sapient.moviebooking.repository;

import com.sapient.moviebooking.entity.SeatReservation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatReservationRepository extends CrudRepository<SeatReservation, String> {
}
