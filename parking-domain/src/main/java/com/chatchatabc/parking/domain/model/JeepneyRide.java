package com.chatchatabc.parking.domain.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "jeepney_ride")
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

    public JeepneyRide() {
    }

    public JeepneyRide(Long id, Long jeepneyId, Long routeId, Long driverId, LocalDateTime startAt, LocalDateTime endAt, Integer status, Integer totalPassengers, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.jeepneyId = jeepneyId;
        this.routeId = routeId;
        this.driverId = driverId;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.totalPassengers = totalPassengers;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJeepneyId() {
        return jeepneyId;
    }

    public void setJeepneyId(Long jeepneyId) {
        this.jeepneyId = jeepneyId;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTotalPassengers() {
        return totalPassengers;
    }

    public void setTotalPassengers(Integer totalPassengers) {
        this.totalPassengers = totalPassengers;
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
}
