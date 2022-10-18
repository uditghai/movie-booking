package com.sapient.moviebooking.dto;

import com.sapient.moviebooking.entity.SeatReservation;
import com.sapient.moviebooking.entity.SeatingClass;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Builder(toBuilder = true, builderClassName = "Builder")
@Data
public class MovieBookingRequest {

    private final long customerId;
    private final int showId;
    private final SeatingClass seatingClass;
    private String sessionId;
    @Size(min = 1, max = 10)
    private List<TheatreSeatRequest> theatreSeatRequests;

    public static MovieBookingRequest of(SeatReservation reservedShowSeat) {
        return MovieBookingRequest.builder()
                .showId(reservedShowSeat.getShowId())
                .sessionId(reservedShowSeat.getId())
                .theatreSeatRequests(reservedShowSeat.getTheatreSeatRequests())
                .build();
    }
}
