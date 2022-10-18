package com.sapient.moviebooking.validation;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@org.springframework.data.rest.core.annotation.RepositoryEventHandler
@NoArgsConstructor
@AllArgsConstructor
public abstract class SimpleRepositoryEventHandler<T> implements RepositoryEventHandler<T> {
    @Autowired
    private Validator validator;

    @Override
    @HandleBeforeCreate
    public void handleBeforeCreate(T object) {
        validate(object);
    }

    @Override
    @HandleBeforeSave
    public void handleBeforeSave(T object) {
        validate(object);
    }

    private void validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
