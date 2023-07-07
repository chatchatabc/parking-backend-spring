package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportStatusRepository extends JpaRepository<ReportStatus, String> {

    /**
     * Find all by report
     *
     * @param report   report
     * @param pageable pageable
     * @return page of report status
     */
    Page<ReportStatus> findAllByReport(Long report, Pageable pageable);
}
