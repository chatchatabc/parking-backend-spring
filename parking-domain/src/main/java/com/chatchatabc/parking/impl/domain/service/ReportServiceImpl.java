package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.Report;
import com.chatchatabc.parking.domain.repository.ReportRepository;
import com.chatchatabc.parking.domain.service.ReportService;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Save report
     *
     * @param report the saved report
     */
    @Override
    public void saveReport(Report report) {
        reportRepository.save(report);
    }
}
