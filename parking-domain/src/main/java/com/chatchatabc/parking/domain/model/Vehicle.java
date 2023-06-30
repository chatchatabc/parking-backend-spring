package com.chatchatabc.parking.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "vehicle")
public class Vehicle {
    public static class VehicleStatus {
        public static final int DELETED = -1;
        public static final int DRAFT = 0;
        public static final int PENDING = 1;
        public static final int REJECTED = 2;
        public static final int VERIFIED = 3;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String vehicleUuid = UUID.randomUUID().toString();

    @Column
    private String name;

    @Column(unique = true)
    private String plateNumber;

    @Column
    private String brandUuid;

    @Column
    private String modelUuid;

    @Column
    private String typeUuid;

    @Column
    private String color;

    @Column
    private String year;

    // TODO: add image for front, back, and side view of the vehicle

    @Column
    private LocalDateTime verifiedAt;

    @JsonIgnore
    @Column
    private Long verifiedBy;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    @Column
    private Integer status = VehicleStatus.DRAFT;

    @JsonIgnore
    @ManyToMany(mappedBy = "vehicles")
    private Collection<User> users;

    @JsonIgnore
    @Column(name = "owner_id")
    private Long owner;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Vehicle() {
    }

    public Vehicle(Long id, String vehicleUuid, String name, String plateNumber, String brandUuid, String modelUuid, String typeUuid, String color, String year, LocalDateTime verifiedAt, Long verifiedBy, String rejectionReason, Integer status, Collection<User> users, Long owner, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.vehicleUuid = vehicleUuid;
        this.name = name;
        this.plateNumber = plateNumber;
        this.brandUuid = brandUuid;
        this.modelUuid = modelUuid;
        this.typeUuid = typeUuid;
        this.color = color;
        this.year = year;
        this.verifiedAt = verifiedAt;
        this.verifiedBy = verifiedBy;
        this.rejectionReason = rejectionReason;
        this.status = status;
        this.users = users;
        this.owner = owner;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehicleUuid() {
        return vehicleUuid;
    }

    public void setVehicleUuid(String vehicleUuid) {
        this.vehicleUuid = vehicleUuid;
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

    public String getBrandUuid() {
        return brandUuid;
    }

    public void setBrandUuid(String brandUuid) {
        this.brandUuid = brandUuid;
    }

    public String getModelUuid() {
        return modelUuid;
    }

    public void setModelUuid(String modelUuid) {
        this.modelUuid = modelUuid;
    }

    public String getTypeUuid() {
        return typeUuid;
    }

    public void setTypeUuid(String typeUuid) {
        this.typeUuid = typeUuid;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public Long getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(Long verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
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
