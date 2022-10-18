package com.sapient.moviebooking.repository;

import com.sapient.moviebooking.entity.Genre;
import com.sapient.moviebooking.entity.Language;
import com.sapient.moviebooking.entity.Movie;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "movies", path = "movies")
public interface MovieRepository extends PagingAndSortingRepository<Movie, Integer> {
    List<Movie> findByNameEquals(String name);

    List<Movie> findByGenresContains(Genre genre);

    List<Movie> findByGenresContainsAndLanguage(Genre genre, Language language);
}
