package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.ReportStatus;
import com.chatchatabc.parking.domain.repository.ReportStatusRepository;
import com.chatchatabc.parking.domain.service.ReportStatusService;
import org.springframework.stereotype.Service;

@Service
public class ReportStatusServiceImpl implements ReportStatusService {
    private final ReportStatusRepository reportStatusRepository;

    public ReportStatusServiceImpl(ReportStatusRepository reportStatusRepository) {
        this.reportStatusRepository = reportStatusRepository;
    }

    /**
     * Save report status
     *
     * @param reportStatus report status
     */
    @Override
    public void saveReportStatus(ReportStatus reportStatus) {
        reportStatusRepository.save(reportStatus);
    }
}
