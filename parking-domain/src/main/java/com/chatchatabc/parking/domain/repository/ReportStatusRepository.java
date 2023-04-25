package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportStatusRepository extends JpaRepository<ReportStatus, String> {
}
