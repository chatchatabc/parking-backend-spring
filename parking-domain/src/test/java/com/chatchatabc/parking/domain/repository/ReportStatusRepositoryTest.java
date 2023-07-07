package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import static org.assertj.core.api.Assertions.assertThat;

class ReportStatusRepositoryTest extends TestContainersBaseTest {
    @Autowired
    private ReportStatusRepository reportStatusRepository;

    @Test
    void testFindAllByReport_ShouldReturnGreaterThan0() {
        // Given
        Long reportId = 1L;
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(reportStatusRepository.findAllByReport(reportId, pr).getTotalElements()).isGreaterThan(0);
    }

    @Test
    void testFindAllByReport_ShouldReturn0() {
        // Given
        Long reportId = 0L;
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(reportStatusRepository.findAllByReport(reportId, pr).getTotalElements()).isEqualTo(0);
    }
}