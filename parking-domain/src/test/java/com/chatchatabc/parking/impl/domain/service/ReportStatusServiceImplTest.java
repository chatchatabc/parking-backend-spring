package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.ReportStatus;
import com.chatchatabc.parking.domain.repository.ReportStatusRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class ReportStatusServiceImplTest extends TestContainersBaseTest {
    @Autowired
    private ReportStatusServiceImpl reportStatusService;
    @Autowired
    private ReportStatusRepository reportStatusRepository;

    @Test
    void testSaveReportStatus_ShouldBeSuccessful() {
        // Given
        ReportStatus reportStatus = new ReportStatus();
        reportStatus.setReport(1L);
        reportStatus.setPerformedBy(1L);
        reportStatus.setStatus(1);
        reportStatus.setRemarks("remarks");
        Long count = reportStatusRepository.count();

        // When
        reportStatusService.saveReportStatus(reportStatus);

        // Then
        assertThat(reportStatusRepository.count()).isGreaterThan(count);
    }
}