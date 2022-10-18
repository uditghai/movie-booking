package com.sapient.moviebooking.validation;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;

public interface RepositoryEventHandler<T> {

    @HandleBeforeCreate
    void handleBeforeCreate(T object);

    @HandleBeforeSave
    void handleBeforeSave(T object);
}
