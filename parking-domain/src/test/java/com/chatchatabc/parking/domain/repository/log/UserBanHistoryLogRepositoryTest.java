package com.chatchatabc.parking.domain.repository.log;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserBanHistoryLogRepositoryTest extends TestContainersBaseTest {
    @Autowired
    private UserBanHistoryLogRepository userBanHistoryLogRepository;

    @Test
    void testFindLatestBanLog_ShouldReturnBanLog() {
        Long userId = 5L;
        assertThat(userBanHistoryLogRepository.findLatestBanLog(userId)).isPresent();
    }

    @Test
    void testFindLatestBanLog_ShouldReturnEmpty() {
        Long userId = 1L;
        assertThat(userBanHistoryLogRepository.findLatestBanLog(userId)).isEmpty();
    }
}