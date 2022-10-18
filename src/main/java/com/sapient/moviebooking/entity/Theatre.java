package com.sapient.moviebooking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.ORDINAL;

@Entity
@Builder(toBuilder = true, builderClassName = "Builder")
@Getter
@ToString
@Setter(AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Theatre {
    @Id
    @GeneratedValue
    private int id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Enumerated(ORDINAL)
    @NotNull
    @Column(nullable = false, name = "cityId")
    private City city;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "theatre")
    @JsonIgnore
    @ToString.Exclude
    private List<Show> runningShows = new ArrayList<>();

    @OneToMany(mappedBy = "theatre", fetch = FetchType.EAGER)
    private List<TheatreSeat> theatreSeats;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ownedByTheatrePartnerId", referencedColumnName = "id")
    private TheatrePartner ownedByTheatrePartner;
}
