package com.chatchatabc.service.domain.model

import jakarta.persistence.*
import lombok.Data
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rate_id")
    open var rate: Rate? = null

    @Column
    open lateinit var name: String

    @Column
    open var latitude: Double = 0.0

    @Column
    open var longitude: Double = 0.0

    @Column
    open var address: String? = null

    @Column
    open var description: String? = null

    @Column
    open var capacity: Int = 0

    @Column
    open var availableSlots: Int = 0

    @Column
    open var businessHourStart: Date? = null

    @Column
    open var businessHourEnd: Date? = null

    @Column
    open var operatingFlag: Int = 0

    @CreationTimestamp
    open lateinit var createdAt: Date

    @UpdateTimestamp
    open lateinit var updatedAt: Date
}