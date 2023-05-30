package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.Report;
import com.chatchatabc.parking.domain.repository.ReportRepository;
import com.chatchatabc.parking.impl.domain.service.ReportServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ReportServiceTest extends TestContainersBaseTest {
    @Autowired
    private ReportServiceImpl reportService;
    @Autowired
    private ReportRepository reportRepository;

    @Test
    void testSaveReport_ShouldSaveSuccessfully() {
        Long currentCount = reportRepository.count();
        Report report = new Report();
        report.setName("Test Report");
        report.setDescription("Test Description");
        report.setPlateNumber("Test Plate Number");
        report.setLatitude(1.0);
        report.setLongitude(1.0);
        report.setReportedBy(1L);
        reportService.saveReport(report);
        assertThat(reportRepository.count()).isGreaterThan(currentCount);
    }
}