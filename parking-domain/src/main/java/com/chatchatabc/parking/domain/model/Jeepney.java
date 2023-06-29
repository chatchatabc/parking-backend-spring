package com.chatchatabc.parking.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "jeepney")
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

    public Jeepney() {
    }

    public Jeepney(Long id, String jeepneyUuid, String name, String plateNumber, Long currentRideId, String routeUuid, String drivers, Integer capacity, Integer availableSlots, Double latitude, Double longitude, Integer status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.jeepneyUuid = jeepneyUuid;
        this.name = name;
        this.plateNumber = plateNumber;
        this.currentRideId = currentRideId;
        this.routeUuid = routeUuid;
        this.drivers = drivers;
        this.capacity = capacity;
        this.availableSlots = availableSlots;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJeepneyUuid() {
        return jeepneyUuid;
    }

    public void setJeepneyUuid(String jeepneyUuid) {
        this.jeepneyUuid = jeepneyUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Long getCurrentRideId() {
        return currentRideId;
    }

    public void setCurrentRideId(Long currentRideId) {
        this.currentRideId = currentRideId;
    }

    public String getRouteUuid() {
        return routeUuid;
    }

    public void setRouteUuid(String routeUuid) {
        this.routeUuid = routeUuid;
    }

    public String getDrivers() {
        return drivers;
    }

    public void setDrivers(String drivers) {
        this.drivers = drivers;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(Integer availableSlots) {
        this.availableSlots = availableSlots;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
