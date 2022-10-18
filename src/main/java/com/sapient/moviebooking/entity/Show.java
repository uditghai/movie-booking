package com.sapient.moviebooking.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Builder(toBuilder = true, builderClassName = "Builder")
@Getter
@Setter(AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Show {
    @NotNull
    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "theatreId")
    private Theatre theatre;

    @NotNull
    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "movieId")
    private Movie movie;


    @NotNull
    @Column(nullable = false)
    private LocalDateTime showTime;

    @NotNull
    @Column(nullable = false)
    private ShowStatus status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedDate;
}
