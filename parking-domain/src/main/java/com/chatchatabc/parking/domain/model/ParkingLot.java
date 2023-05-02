package com.chatchatabc.parking.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "parking_lots")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ParkingLot extends FlagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String parkingLotId = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rate_id")
    private Rate rate;

    @Column
    private String name;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column
    private String address;

    @Column
    private String description;

    @Column
    private Integer capacity;

    @Column
    private Integer availableSlots;

    @Column
    private LocalDateTime businessHoursStart;

    @Column
    private LocalDateTime businessHoursEnd;

    @Column
    private LocalDateTime verifiedAt;

    // Verified by user
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "verified_by")
    private User verifiedBy;
}
