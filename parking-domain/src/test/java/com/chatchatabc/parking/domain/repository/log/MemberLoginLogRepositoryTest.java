package com.chatchatabc.parking.domain.repository.log;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

class MemberLoginLogRepositoryTest extends TestContainersBaseTest {
    @Autowired
    private MemberLoginLogRepository memberLoginLogRepository;

    @Test
    void testFindByMember_ShouldReturnGreaterThan0() {
        Long memberId = 6L;
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(memberLoginLogRepository.findByMember(memberId, pr).getTotalElements()).isGreaterThan(0L);
    }
}