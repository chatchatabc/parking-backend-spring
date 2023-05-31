package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    /**
     * Find all reports by user id
     *
     * @param userId   user id
     * @param pageable pagination
     * @return page of reports
     */
    Page<Report> findAllByReportedBy(Long userId, Pageable pageable);
}
