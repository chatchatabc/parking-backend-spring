package com.chatchatabc.service.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lombok.Data
import org.hibernate.annotations.CreationTimestamp
import java.util.*

@Data
@Entity
@Table(name = "report_statuses")
open class ReportStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open lateinit var id: String

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "report_id")
    open lateinit var report: Report

    @ManyToOne
    @JoinColumn(name = "performed_by")
    open lateinit var performedBy: User

    @Column
    open var status: Int = 0

    @Column
    open var remarks: String? = null

    @CreationTimestamp
    open lateinit var createdAt: Date
}