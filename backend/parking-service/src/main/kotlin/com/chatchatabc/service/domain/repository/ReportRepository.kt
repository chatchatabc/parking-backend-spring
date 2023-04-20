package com.chatchatabc.service.domain.repository

import com.chatchatabc.service.domain.model.Report
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportRepository : JpaRepository<Report, String> {
}