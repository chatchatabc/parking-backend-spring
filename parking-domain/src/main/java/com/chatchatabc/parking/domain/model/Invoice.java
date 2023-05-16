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
@Table(name = "invoice")
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JsonIgnore
    @Column(name = "parking_lot_id")
    private Long parkingLot;

    @JsonIgnore
    @Column(name = "vehicle_id")
    private Long vehicle;

    @Column
    private String plateNumber;

    @Column
    private int estimatedParkingDurationInHours;

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
}
