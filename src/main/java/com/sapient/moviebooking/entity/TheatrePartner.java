package com.sapient.moviebooking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder(toBuilder = true, builderClassName = "Builder")
@Getter
@Setter(AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class TheatrePartner {
    @Id
    @GeneratedValue
    private int id;
    @Column(length = 36, nullable = false)
    private String code;
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "ownedByTheatrePartner")
    @JsonIgnore
    private List<Theatre> theatres;
}
