package com.chatchatabc.parking.domain.model

import jakarta.persistence.*
import lombok.Data
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*

@Data
@Entity
@Table(name = "reports")
open class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open lateinit var id: String

    @Column
    open lateinit var name: String

    @Column
    open lateinit var description: String

    @Column
    open lateinit var plateNumber: String

    @Column
    open var latitude: Double = 0.0

    @Column
    open var longitude: Double = 0.0

    @ManyToOne
    @JoinColumn(name = "reported_by")
    open lateinit var reportedBy: User

    @Column
    open var cancelledAt: Date? = null

    @OneToMany(mappedBy = "report", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    open var reportStatuses: MutableList<ReportStatus> = mutableListOf()

    @CreationTimestamp
    open lateinit var createdAt: Date

    @UpdateTimestamp
    open lateinit var updatedAt: Date
}