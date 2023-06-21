package com.chatchatabc.parking.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "jeepney")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Jeepney extends FlagEntity {
    public static class JeepneyStatus {
        public static final int INACTIVE = -1;
        public static final int DRAFT = 0;
        public static final int ACTIVE = 1;
    }

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: Rename to vkey
    @Column(unique = true)
    private String jeepneyUuid;

    // TODO: Add device ID

    @Column
    private String name;

    @Column(unique = true)
    private String plateNumber;

    @Column
    private Long currentRideId;

    @Column
    private String routeUuid;

    @Column
    private String drivers;

    @Column
    private Integer capacity = 0;

    @Column
    private Integer availableSlots = 0;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column
    private Integer status = JeepneyStatus.DRAFT;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
