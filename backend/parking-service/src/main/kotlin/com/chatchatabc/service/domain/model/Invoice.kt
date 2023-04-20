package com.chatchatabc.service.domain.model

import jakarta.persistence.*
import lombok.Data
import org.hibernate.annotations.CreationTimestamp
import java.math.BigDecimal
import java.util.*

@Data
@Entity
@Table(name = "invoices")
open class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open lateinit var id: String

    @ManyToOne
    @JoinColumn(name = "parking_lot_id")
    open lateinit var parkingLot: ParkingLot

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    open var vehicle: Vehicle? = null

    @Column
    open var plateNumber: String? = null

    @Column
    open var estimatedParkingDurationInHours: Int = 0

    @Column(columnDefinition = "DECIMAL(10,2)")
    open var total: BigDecimal? = null

    @Column
    open var paidAt: Date? = null

    @Column
    open var startAt: Date? = null

    @Column
    open var endAt: Date? = null

    @CreationTimestamp
    open lateinit var createdAt: Date

    @CreationTimestamp
    open lateinit var updatedAt: Date
}