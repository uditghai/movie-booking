package com.sapient.moviebooking;

import java.util.ConcurrentModificationException;

public class ConcurrentReservationException extends ConcurrentModificationException {
    public ConcurrentReservationException(String message) {
        super(message);
    }
}
