package com.chatchatabc.parking.domain.repository.log;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MemberBanHistoryLogRepositoryTest extends TestContainersBaseTest {
    @Autowired
    private MemberBanHistoryLogRepository memberBanHistoryLogRepository;

    @Test
    void testFindLatestBanLog_ShouldReturnBanLog() {
        Long memberId = 5L;
        assertThat(memberBanHistoryLogRepository.findLatestBanLog(memberId)).isPresent();
    }

    @Test
    void testFindLatestBanLog_ShouldReturnEmpty() {
        Long memberId = 1L;
        assertThat(memberBanHistoryLogRepository.findLatestBanLog(memberId)).isEmpty();
    }
}