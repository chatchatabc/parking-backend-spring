package com.chatchatabc.parking.domain.repository

import com.chatchatabc.parking.domain.model.Report
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository : JpaRepository<Report, String> {
}