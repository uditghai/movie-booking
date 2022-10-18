package com.sapient.moviebooking.repository;

import com.sapient.moviebooking.entity.City;
import com.sapient.moviebooking.entity.Movie;
import com.sapient.moviebooking.entity.Show;
import com.sapient.moviebooking.entity.projection.ShowDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "shows", path = "shows", excerptProjection =
        ShowDetail.class)
public interface ShowRepository extends PagingAndSortingRepository<Show, Integer> {
    @Query(name = "Show.findShowsByMovieAndCityAndDate")
    List<Show> findShowsByMovieAndCityAndDate(
            @NotNull @Param("movie") Movie movie,
            @NotNull @Param("city") City city,
            @NotNull @DateTimeFormat(pattern = "yyyyMMdd") @Param("date") LocalDate date);
}
