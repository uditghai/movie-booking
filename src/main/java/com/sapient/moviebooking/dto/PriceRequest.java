package com.sapient.moviebooking.dto;

import com.sapient.moviebooking.entity.SeatingClass;
import com.sapient.moviebooking.entity.Show;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true, builderClassName = "Builder")
@Data
@AllArgsConstructor(staticName = "of")
public class PriceRequest {
    private final long customerId;
    private final Show show;
    private final SeatingClass seatingClass;
}
