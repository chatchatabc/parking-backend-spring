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
    public static class RateType {
        public static final int FIXED = 0;
        public static final int FLEXIBLE = 1;
    }

    public static class RateInterval {
        public static final int HOURLY = 0;
        public static final int DAILY = 1;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JsonIgnore
    @OneToOne(mappedBy = "rate", fetch = FetchType.LAZY)
    private ParkingLot parkingLot;

    @Column
    private int type = RateType.FIXED;

    @Column
    private int interval = RateInterval.HOURLY;

    @Column
    private int freeHours = 0;

    @Column
    private boolean payForFreeHoursWhenExceeding = false;

    @Column(columnDefinition = "DECIMAL(10,2)")
    private BigDecimal startingRate;

    @Column(columnDefinition = "DECIMAL(10,2)")
    private BigDecimal rate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
