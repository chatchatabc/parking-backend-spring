package com.chatchatabc.parking.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rate")
public class Rate {
    public static class RateType {
        public static final int FIXED = 0;
        public static final int FLEXIBLE = 1;
    }

    public static class RateInterval {
        public static final int HOURLY = 0;
        public static final int DAILY = 1;
    }

    // TODO: Add currency field

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getFreeHours() {
        return freeHours;
    }

    public void setFreeHours(int freeHours) {
        this.freeHours = freeHours;
    }

    public boolean isPayForFreeHoursWhenExceeding() {
        return payForFreeHoursWhenExceeding;
    }

    public void setPayForFreeHoursWhenExceeding(boolean payForFreeHoursWhenExceeding) {
        this.payForFreeHoursWhenExceeding = payForFreeHoursWhenExceeding;
    }

    public BigDecimal getStartingRate() {
        return startingRate;
    }

    public void setStartingRate(BigDecimal startingRate) {
        this.startingRate = startingRate;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Rate() {
    }

    public Rate(String id, ParkingLot parkingLot, int type, int interval, int freeHours, boolean payForFreeHoursWhenExceeding, BigDecimal startingRate, BigDecimal rate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.parkingLot = parkingLot;
        this.type = type;
        this.interval = interval;
        this.freeHours = freeHours;
        this.payForFreeHoursWhenExceeding = payForFreeHoursWhenExceeding;
        this.startingRate = startingRate;
        this.rate = rate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
