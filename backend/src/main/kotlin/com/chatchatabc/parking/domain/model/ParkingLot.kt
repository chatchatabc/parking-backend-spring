package com.chatchatabc.parking.domain.model

import jakarta.persistence.*
import lombok.Data
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.util.*

@Data
@Entity
@Table(name = "parking_lots")
open class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open lateinit var id: String

    @ManyToOne
    @JoinColumn(name = "owner_id")
    open lateinit var owner: User

    @Column
    open lateinit var name: String

    @Column(columnDefinition = "DECIMAL(10,2)")
    open lateinit var rate: BigDecimal

    @Column
    open var capacity: Int = 0

    @Column
    open var availableSlots: Int = 0

    @Column
    open var latitude: Double = 0.0

    @Column
    open var longitude: Double = 0.0

    @CreationTimestamp
    open lateinit var createdAt: Date

    @UpdateTimestamp
    open lateinit var updatedAt: Date
}