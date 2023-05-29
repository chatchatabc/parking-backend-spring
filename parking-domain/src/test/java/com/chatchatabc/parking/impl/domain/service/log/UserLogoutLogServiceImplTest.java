package com.chatchatabc.parking.impl.domain.service.log;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.repository.log.UserLogoutLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class UserLogoutLogServiceImplTest extends TestContainersBaseTest {
    @Autowired
    private UserLogoutLogServiceImpl userLogoutLogService;
    @Autowired
    private UserLogoutLogRepository userLogoutLogRepository;

    @Test
    void testCreateLog_ShouldCreateLogoutLog() {
        Long userId = 1L;
        String ipAddress = "127.0.0.1";
        Integer type = 1;

        Long currentCount = userLogoutLogRepository.count();
        userLogoutLogService.createLog(userId, type, ipAddress);
        assertThat(userLogoutLogRepository.count()).isGreaterThan(currentCount);
    }
}