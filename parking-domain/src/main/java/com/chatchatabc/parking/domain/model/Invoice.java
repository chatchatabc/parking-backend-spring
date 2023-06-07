package com.chatchatabc.parking.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "invoice")
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
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
