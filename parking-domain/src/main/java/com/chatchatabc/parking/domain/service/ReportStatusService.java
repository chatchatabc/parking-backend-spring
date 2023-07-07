package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.model.ReportStatus;

public interface ReportStatusService {

    /**
     * Save report status
     *
     * @param reportStatus report status
     */
    void saveReportStatus(ReportStatus reportStatus);
}
