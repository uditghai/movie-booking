package com.sapient.moviebooking.controller;

import com.sapient.moviebooking.entity.City;
import com.sapient.moviebooking.entity.Show;
import com.sapient.moviebooking.repository.MovieRepository;
import com.sapient.moviebooking.repository.ShowRepository;
import com.sapient.moviebooking.repository.TheatreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.sapient.moviebooking.entity.ShowStatus.OpenForBooking;
import static org.springframework.data.util.Streamable.of;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/data")
public class DataLoadController {
    @Autowired
    private final ShowRepository showRepository;
    @Autowired
    private final TheatreRepository theatreRepository;

    @Autowired
    private final MovieRepository movieRepository;

    private final Random rand = new Random();

    @PostMapping("/movie/{movieId}")
    public int createBulkShows(final @NotNull @PathVariable Integer movieId,
                               final @RequestParam @DateTimeFormat(pattern = "yyyyMMdd") Optional<LocalDate> date,
                               final @RequestParam Optional<List<City>> cities,
                               final @RequestBody Optional<List<Integer>> theatreIds) {

        var movie = movieRepository.findById(movieId).orElseThrow();

        var theatreList =
                cities.map(theatreRepository::findByCityIn)
                        .orElseGet(() -> theatreIds
                                .map(ids -> of(theatreRepository.findAllById(ids)).toList())
                                .orElse(theatreRepository.findAll()));

        LocalDate showDate = date.orElse(LocalDate.now());

        List<Show> savedShows = new ArrayList<>();
        log.info("theatreList = {}", theatreList);
        theatreList.forEach(
                theatre -> {
                    var show = Show.builder()
                            .theatre(theatre)
                            .movie(movie)
                            .showTime(showDate.atTime(
                                    rand.nextInt(8, 22),
                                    rand.nextInt(0, 6) * 10,
                                    0))
                            .status(OpenForBooking)
                            .build();
                    Show savedShow = showRepository.save(show);
                    savedShows.add(savedShow);
                });
        return savedShows.size();
    }
}
