package com.chatchatabc.parking.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "rate")
@AllArgsConstructor
@NoArgsConstructor
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JsonIgnore
    @OneToOne(mappedBy = "rate", fetch = FetchType.LAZY)
    private ParkingLot parkingLot;

    @Column
    private int type;

    @Column
    private int interval;

    @Column
    private int freeHours;

    @Column
    private boolean payForFreeHoursWhenExceeding;

    @Column(columnDefinition = "DECIMAL(10,2)")
    private BigDecimal startingRate;

    @Column(columnDefinition = "DECIMAL(10,2)")
    private BigDecimal rate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
