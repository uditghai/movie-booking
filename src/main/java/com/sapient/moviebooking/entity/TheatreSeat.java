package com.sapient.moviebooking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Builder(toBuilder = true, builderClassName = "Builder")
@Getter
@Setter(AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UQ_TheatreSeat_A", columnNames = {"theatreId", "row", "seatNo"})})
public class TheatreSeat {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "theatreId", referencedColumnName = "id")
    @JsonIgnore
    private Theatre theatre;

    @NotNull
    @Column(nullable = false, length = 1)
    private String row;

    @NotNull
    @Column(nullable = false)
    private int seatNo;
}
