package com.chatchatabc.parking.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lombok.Data
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.math.BigDecimal
import java.util.*

@Data
@Entity
@Table(name = "rates")
@EnableJpaAuditing
open class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open lateinit var id: String

    @JsonIgnore
    @OneToOne(mappedBy = "rate", fetch = FetchType.LAZY)
    open lateinit var parkingLot: ParkingLot

    @Column
    open var type: Int = 0

    @Column
    open var interval: Int = 0

    @Column
    open var freeHours: Int = 0

    @Column
    open var payForFreeHoursWhenExceeding: Boolean = false

    @Column(columnDefinition = "DECIMAL(10,2)")
    open var startingRate: BigDecimal = 0.0.toBigDecimal()

    @Column(columnDefinition = "DECIMAL(10,2)")
    open var rate: BigDecimal = 0.0.toBigDecimal()

    @CreationTimestamp
    open lateinit var createdAt: Date

    @UpdateTimestamp
    open lateinit var updatedAt: Date

    @Version
    @Column(name = "version", nullable = false)
    open var version: Long? = null
}