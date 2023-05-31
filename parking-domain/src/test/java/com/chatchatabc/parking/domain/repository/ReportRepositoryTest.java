package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

class ReportRepositoryTest extends TestContainersBaseTest {
    @Autowired
    private ReportRepository reportRepository;

    @Test
    void testFindAllByReportedBy_ShouldReturnGreaterThan0() {
        Long userId = 1L;
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(reportRepository.findAllByReportedBy(userId, pr).getTotalElements()).isGreaterThan(0L);
    }

    @Test
    void testFindAllByReportedBy_ShouldReturn0() {
        Long userId = 100L;
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(reportRepository.findAllByReportedBy(userId, pr).getTotalElements()).isEqualTo(0L);
    }
}