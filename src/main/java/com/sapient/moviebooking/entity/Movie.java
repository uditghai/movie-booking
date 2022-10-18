package com.sapient.moviebooking.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;


@Entity
@Table(name = "Movie", indexes = {
        @Index(name = "idx_movie_language", columnList = "language")
})
@Builder(toBuilder = true, builderClassName = "Builder")
@Getter
@Setter(AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Movie {
    @ElementCollection(targetClass = Genre.class)
    @Column(name = "genre", nullable = false)
    @Enumerated(EnumType.STRING)
    @UniqueElements
    @Valid
    @CollectionTable(name = "movieGenres", joinColumns = @JoinColumn(name = "movieId"),
            indexes = @Index(columnList = "genre"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"movieId", "genre"}))
    Collection<Genre> genres;
    @NotNull
    @Id
    @GeneratedValue
    private int id;
    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Valid
    private Language language;
    @NotNull
    @Column(nullable = false)
    private int runTime = 0;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedDate;
}
