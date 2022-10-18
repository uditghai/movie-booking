package com.sapient.moviebooking.convertor;

import com.sapient.moviebooking.entity.Show;
import com.sapient.moviebooking.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IntToShowConverter implements Converter<Integer, Show> {
    @Autowired
    private final ShowRepository showRepository;

    @Override
    public Show convert(Integer source) {
        return showRepository.findById(source).orElseThrow();
    }
}
