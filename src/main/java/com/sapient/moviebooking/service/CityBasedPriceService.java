package com.sapient.moviebooking.service;

import com.sapient.moviebooking.dto.PriceRequest;
import com.sapient.moviebooking.entity.City;
import com.sapient.moviebooking.entity.SeatingClass;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Service
public class CityBasedPriceService implements PriceService {

    private final Map<City, Double> cityMarkUp = Map.of(
            City.Chandigarh, 1d,
            City.NewDelhi, 1.2d,
            City.Bangalore, 1.1d,
            City.Mumbai, 1.5d);
    private final Map<SeatingClass, Double> seatingClassMarkUp = Map.of(
            SeatingClass.Standard, 1d,
            SeatingClass.Executive, 1.2d,
            SeatingClass.Recliner, 1.5d);

    @Override
    public double getTicketPrice(@NotNull @Valid PriceRequest priceRequest) {
        double singleTicketPrice = 100;
        return singleTicketPrice
                * cityMarkUp.getOrDefault(priceRequest.getShow().getTheatre().getCity(), 1d)
                * seatingClassMarkUp.getOrDefault(priceRequest.getSeatingClass(), 1d);
    }
}
