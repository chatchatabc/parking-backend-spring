package com.chatchatabc.parking.impl.domain.service.log;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.repository.log.UserLoginLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class UserLoginLogServiceImplTest extends TestContainersBaseTest {
    @Autowired
    private UserLoginLogServiceImpl userLoginLogService;
    @Autowired
    private UserLoginLogRepository userLoginLogRepository;

    @Test
    void testCreatedLog_ShouldCreateLoginLog() {
        Long userId = 1L;
        String ipAddress = "127.0.0.1";
        Integer type = 1;
        Boolean success = true;

        Long currentCount = userLoginLogRepository.count();
        userLoginLogService.createLog(userId, ipAddress, type, success);
        assertThat(userLoginLogRepository.count()).isGreaterThan(currentCount);
    }
}