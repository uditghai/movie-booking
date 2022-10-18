package com.sapient.moviebooking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private final Function<ConstraintViolation, ValidationError> formatConstraintViolation = (ConstraintViolation constraintViolation) -> new ValidationError(constraintViolation.getPropertyPath().toString(), constraintViolation.getInvalidValue().toString(), constraintViolation.getMessage());

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    Set<ValidationError> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error(ex.getMessage(), ex);
        return ex.getConstraintViolations().stream().map(formatConstraintViolation).collect(Collectors.toUnmodifiableSet());
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    ValidationError handleNoSuchElementException(NoSuchElementException ex) {
        log.error(ex.getMessage(), ex);
        return new ValidationError(null, null, ex.getLocalizedMessage());
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    ValidationError handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);
        return new ValidationError(null, null, ex.getLocalizedMessage());
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(ConcurrentReservationException.class)
    ValidationError handleConcurrentReservationException(ConcurrentReservationException ex) {
        log.error(ex.getMessage(), ex);
        return new ValidationError("", "", ex.getLocalizedMessage());
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(SeatNoLongerReservedException.class)
    ValidationError handleSeatNoLongerReservedException(SeatNoLongerReservedException ex) {
        log.error(ex.getMessage(), ex);
        return new ValidationError("", "", ex.getLocalizedMessage());
    }


    record ValidationError(String field, Object values, String errorMessage) {
    }
}
