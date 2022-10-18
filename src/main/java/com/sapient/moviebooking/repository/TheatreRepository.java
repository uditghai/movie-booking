package com.sapient.moviebooking.repository;

import com.sapient.moviebooking.entity.City;
import com.sapient.moviebooking.entity.Theatre;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "theatres", path = "theatres")
public interface TheatreRepository extends PagingAndSortingRepository<Theatre, Integer> {
    List<Theatre> findByCityIn(List<City> cities);

    List<Theatre> findAll();
}
