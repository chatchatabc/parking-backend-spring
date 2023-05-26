package com.chatchatabc.parking.impl.domain.service.log;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.repository.log.MemberLogoutLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class MemberLogoutLogServiceImplTest extends TestContainersBaseTest {
    @Autowired
    private MemberLogoutLogServiceImpl memberLogoutLogService;
    @Autowired
    private MemberLogoutLogRepository memberLogoutLogRepository;

    @Test
    void testCreateLog_ShouldCreateLogoutLog() {
        Long memberId = 1L;
        String ipAddress = "127.0.0.1";
        Integer type = 1;

        Long currentCount = memberLogoutLogRepository.count();
        memberLogoutLogService.createLog(memberId, type, ipAddress);
        assertThat(memberLogoutLogRepository.count()).isGreaterThan(currentCount);
    }
}