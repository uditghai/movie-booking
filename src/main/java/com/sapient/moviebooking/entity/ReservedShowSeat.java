package com.sapient.moviebooking.entity;

import lombok.*;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static java.lang.Integer.toHexString;
import static java.util.concurrent.TimeUnit.SECONDS;

@Getter
@Setter(AccessLevel.PACKAGE)
@ToString
@NoArgsConstructor(access = AccessLevel.PACKAGE, staticName = "of")
@AllArgsConstructor(access = AccessLevel.PACKAGE, staticName = "of")
@Builder(toBuilder = true, builderClassName = "Builder")
public class ReservedShowSeat implements Serializable {
    private final static String prefix = "ReservedSeat";
    private int showId;
    private String row;
    private int seatNo;
    @Indexed
    private String reservationId;
    @TimeToLive(unit = SECONDS)
    private long timeToLive = -1;

    public static List<ReservedShowSeat> of(SeatReservation seatReservation, int timeToLive) {
        return seatReservation.getTheatreSeatRequests().stream().map(seat ->
                ReservedShowSeat.of(seatReservation.getShowId(),
                        seat.getRow(), seat.getSeatNo(), seatReservation.getId(), timeToLive)).toList();
    }

    public static List<ReservedShowSeat> of(SeatReservation seatReservation) {
        return seatReservation.getTheatreSeatRequests().stream().map(seat ->
                ReservedShowSeat.of(seatReservation.getShowId(),
                        seat.getRow(), seat.getSeatNo(), seatReservation.getId(), -1)).toList();
    }

    @Id
    @AccessType(value = AccessType.Type.PROPERTY)
    public String getId() {
        return concatenatedId();
    }

    public String getId(boolean showHexString) {
        return showHexString ? hexStringId() : concatenatedId();
    }

    private String hexStringId() {
        return "%s:%s".formatted(prefix, toHexString(Objects.hash(showId, row, seatNo)));
    }

    private String concatenatedId() {
        return "%s:%d:%s:%d".formatted(prefix, showId, row, seatNo);
    }
}
