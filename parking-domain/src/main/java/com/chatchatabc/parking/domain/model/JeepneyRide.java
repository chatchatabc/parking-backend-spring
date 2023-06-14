package com.chatchatabc.parking.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "jeepney_ride")
@AllArgsConstructor
@NoArgsConstructor
public class JeepneyRide extends FlagEntity {
    public static class JeepneyRideStatus {
        public static final int INACTIVE = -1;
        public static final int ACTIVE = 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long jeepneyId;

    @Column
    private Long routeId;

    @Column
    private Long driverId;

    @Column
    private LocalDateTime startAt;

    @Column
    private LocalDateTime endAt;

    @Column
    private Integer status = JeepneyRideStatus.ACTIVE;

    @Column
    private Integer totalPassengers = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
