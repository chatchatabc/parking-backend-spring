package com.chatchatabc.parking.impl.domain.service.log;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.repository.log.MemberLoginLogRepository;
import com.chatchatabc.parking.domain.service.log.MemberLoginLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class MemberLoginLogServiceImplTest extends TestContainersBaseTest {
    @Autowired
    private MemberLoginLogService memberLoginLogService;
    @Autowired
    private MemberLoginLogRepository memberLoginLogRepository;

    @Test
    void testCreatedLog_ShouldCreateLoginLog() {
        Long memberId = 1L;
        String ipAddress = "127.0.0.1";
        Integer type = 1;
        Boolean success = true;

        Long currentCount = memberLoginLogRepository.count();
        memberLoginLogService.createLog(memberId, ipAddress, type, success);
        assertThat(memberLoginLogRepository.count()).isGreaterThan(currentCount);
    }
}