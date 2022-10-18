package com.sapient.moviebooking.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sapient.moviebooking.entity.ReservedShowSeat;
import com.sapient.moviebooking.entity.TheatreSeat;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor(staticName = "of")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TheatreSeatRequest {
    @NotNull
    private final String row;
    @NotNull
    private final int seatNo;

    public static TheatreSeatRequest of(ReservedShowSeat reservedShowSeat) {
        return new TheatreSeatRequest(reservedShowSeat.getRow(), reservedShowSeat.getSeatNo());
    }

    public static TheatreSeatRequest of(TheatreSeat theatreSeat) {
        return new TheatreSeatRequest(theatreSeat.getRow(), theatreSeat.getSeatNo());
    }
}
