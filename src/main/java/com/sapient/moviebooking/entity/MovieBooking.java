package com.sapient.moviebooking.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder(toBuilder = true, builderClassName = "Builder")
@Getter
@Setter(AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class MovieBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(nullable = false)
    private long customerId;

    @ManyToOne
    @JoinColumn(name = "showId", nullable = false, referencedColumnName = "id")
    @NotNull
    @Valid
    private Show show;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatingClass seatingClass;

    @Max(10)
    @Min(1)
    private int noOfTickets;

    @NotNull
    @Column(nullable = false)
    private double price;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus bookingStatus;

    @NotNull
    @Column(nullable = false)
    private String sessionId;

    @Size(min = 1, max = 10)
    @ManyToMany
    @JoinTable
    private List<TheatreSeat> theatreSeats;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedDate;
}
