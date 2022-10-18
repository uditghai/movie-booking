package com.sapient.moviebooking.entity.projection;

import com.sapient.moviebooking.entity.Movie;
import com.sapient.moviebooking.entity.Show;
import com.sapient.moviebooking.entity.ShowStatus;
import com.sapient.moviebooking.entity.Theatre;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

@Projection(
        name = "ShowDetail",
        types = {Show.class})
public interface ShowDetail {
    int getId();

    Theatre getTheatre();

    Movie getMovie();

    LocalDateTime getShowTime();

    ShowStatus getStatus();

}
