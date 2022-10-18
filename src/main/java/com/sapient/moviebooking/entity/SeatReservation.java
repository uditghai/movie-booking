package com.sapient.moviebooking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sapient.moviebooking.dto.MovieBookingRequest;
import com.sapient.moviebooking.dto.TheatreSeatRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@RedisHash("SeatReservation")
@AllArgsConstructor
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeatReservation {
    @Id
    @NotNull
    @JsonProperty(required = true)
    private final String id;

    @NotNull
    @JsonProperty(required = true)
    private final int showId;

    @NotNull
    @UniqueElements
    private final List<TheatreSeatRequest> theatreSeatRequests;

    @CreatedDate
    @JsonProperty(required = true)
    private LocalDateTime createdDate;

    public static SeatReservation of(MovieBookingRequest movieBookingRequest) {
        return SeatReservation.builder()
                .showId(movieBookingRequest.getShowId())
                .id(movieBookingRequest.getSessionId())
                .theatreSeatRequests(movieBookingRequest.getTheatreSeatRequests())
                .createdDate(LocalDateTime.now())
                .build();
    }
    public static SeatReservation of(SeatReservation seatReservation) {
        return seatReservation.toBuilder().createdDate(LocalDateTime.now()).build();
    }
}
