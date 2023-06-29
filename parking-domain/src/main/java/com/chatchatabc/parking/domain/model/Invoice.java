package com.chatchatabc.parking.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "invoice")
public class Invoice {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String invoiceUuid = UUID.randomUUID().toString();

    @Column(name = "parking_lot_uuid")
    private String parkingLotUuid;

    @Column(name = "vehicle_uuid")
    private String vehicleUuid;

    @Column
    private int estimatedParkingDurationInHours;

    @Column
    private LocalDateTime estimatedEndAt;

    @Column(columnDefinition = "DECIMAL(10,2)")
    private BigDecimal total;

    @Column
    private LocalDateTime paidAt;

    @Column
    private LocalDateTime startAt;

    @Column
    private LocalDateTime endAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceUuid() {
        return invoiceUuid;
    }

    public void setInvoiceUuid(String invoiceUuid) {
        this.invoiceUuid = invoiceUuid;
    }

    public String getParkingLotUuid() {
        return parkingLotUuid;
    }

    public void setParkingLotUuid(String parkingLotUuid) {
        this.parkingLotUuid = parkingLotUuid;
    }

    public String getVehicleUuid() {
        return vehicleUuid;
    }

    public void setVehicleUuid(String vehicleUuid) {
        this.vehicleUuid = vehicleUuid;
    }

    public int getEstimatedParkingDurationInHours() {
        return estimatedParkingDurationInHours;
    }

    public void setEstimatedParkingDurationInHours(int estimatedParkingDurationInHours) {
        this.estimatedParkingDurationInHours = estimatedParkingDurationInHours;
    }

    public LocalDateTime getEstimatedEndAt() {
        return estimatedEndAt;
    }

    public void setEstimatedEndAt(LocalDateTime estimatedEndAt) {
        this.estimatedEndAt = estimatedEndAt;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
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

    public Invoice() {
    }

    public Invoice(Long id, String invoiceUuid, String parkingLotUuid, String vehicleUuid, int estimatedParkingDurationInHours, LocalDateTime estimatedEndAt, BigDecimal total, LocalDateTime paidAt, LocalDateTime startAt, LocalDateTime endAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.invoiceUuid = invoiceUuid;
        this.parkingLotUuid = parkingLotUuid;
        this.vehicleUuid = vehicleUuid;
        this.estimatedParkingDurationInHours = estimatedParkingDurationInHours;
        this.estimatedEndAt = estimatedEndAt;
        this.total = total;
        this.paidAt = paidAt;
        this.startAt = startAt;
        this.endAt = endAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
