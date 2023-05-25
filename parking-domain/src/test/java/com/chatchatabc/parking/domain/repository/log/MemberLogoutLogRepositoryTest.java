package com.chatchatabc.parking.domain.repository.log;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class MemberLogoutLogRepositoryTest extends TestContainersBaseTest {
    @Autowired
    private MemberLogoutLogRepository memberLogoutLogRepository;

    @Test
    void testFindByMember_ShouldReturnGreaterThan0() {
        Long memberId = 6L;
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(memberLogoutLogRepository.findByMember(memberId, pr).getTotalElements()).isGreaterThan(0);
    }

    @Test
    void testFindByMember_ShouldReturn0() {
        Long memberId = 1L;
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(memberLogoutLogRepository.findByMember(memberId, pr).getTotalElements()).isEqualTo(0);
    }
}