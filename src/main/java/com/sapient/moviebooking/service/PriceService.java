package com.sapient.moviebooking.service;

import com.sapient.moviebooking.dto.PriceRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface PriceService {
    double getTicketPrice(@NotNull @Valid PriceRequest priceRequest);
}
