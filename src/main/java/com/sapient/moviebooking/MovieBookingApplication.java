package com.sapient.moviebooking;

import com.sapient.moviebooking.entity.City;
import com.sapient.moviebooking.entity.Theatre;
import com.sapient.moviebooking.entity.TheatrePartner;
import com.sapient.moviebooking.repository.TheatrePartnerRepository;
import com.sapient.moviebooking.repository.TheatreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static com.sapient.moviebooking.entity.City.*;
import static org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy.RepositoryDetectionStrategies.ANNOTATED;

@SpringBootApplication
@Slf4j
public class MovieBookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieBookingApplication.class, args);
    }

    @Bean
    public RepositoryDetectionStrategy repositoryDetectionStrategy() {
        return ANNOTATED;
    }

    @Bean
    public CommandLineRunner initialiseTheatre(TheatreRepository repo, TheatrePartnerRepository theatrePartnerRepository) {
        return args -> {
            if (repo.count() == 0) {
                log.info("Adding new default theatre partner...");
                TheatrePartner initialTheatrePartner = TheatrePartner.builder()
                        .code(UUID.randomUUID().toString())
                        .name("PVR " + "Cinemas")
                        .build();
                theatrePartnerRepository.save(initialTheatrePartner);
            }

            TheatrePartner defaultTheatrePartner = StreamSupport.stream(theatrePartnerRepository.findAll()
                            .spliterator(), false)
                    .findAny().orElseThrow();

            if (repo.count() == 0) {
                log.info("Adding new default theatres...");
                List<Theatre> theatres = new ArrayList<>();
                theatres.add(getTheatre(Chandigarh, defaultTheatrePartner));
                theatres.add(getTheatre(NewDelhi, defaultTheatrePartner));
                theatres.add(getTheatre(Gurugram, defaultTheatrePartner));
                theatres.add(getTheatre(Bangalore, defaultTheatrePartner));
                theatres.add(getTheatre(Pune, defaultTheatrePartner));
                repo.saveAll(theatres);
            }
        };
    }

    private Theatre getTheatre(City city, TheatrePartner theatrePartner) {
        return Theatre.builder().city(city).ownedByTheatrePartner(theatrePartner).name("%s - %s".formatted(theatrePartner.getName(), city.name())).build();
    }
}
