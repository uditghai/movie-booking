package com.sapient.moviebooking.service;

import com.sapient.moviebooking.entity.Theatre;
import com.sapient.moviebooking.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class TheatrePartnerService implements CrudService<Theatre, Integer> {
    @Autowired
    TheatreRepository theatreRepository;

    @Override
    public Theatre save(Theatre entity) {
        return theatreRepository.save(entity);
    }

    @Override
    public Optional<Theatre> findById(Integer id) {
        return theatreRepository.findById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return theatreRepository.existsById(id);
    }

    @Override
    public Collection<Theatre> findAll() {
        return theatreRepository.findAll().stream().toList();
    }
}
