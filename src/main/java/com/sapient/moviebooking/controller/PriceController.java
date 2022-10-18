package com.sapient.moviebooking.controller;

import com.sapient.moviebooking.dto.PriceRequest;
import com.sapient.moviebooking.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;


@RestController
@RequiredArgsConstructor
@RequestMapping("/price")
public class PriceController {
    @Autowired
    private final PriceService priceService;

    @PostMapping
    public double getPrice(@RequestBody @NotNull PriceRequest priceRequest) {
        return priceService.getTicketPrice(priceRequest);
    }
}
