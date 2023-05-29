package com.chatchatabc.parking.domain.repository.log;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserLogoutLogRepositoryTest extends TestContainersBaseTest {
    @Autowired
    private UserLogoutLogRepository userLogoutLogRepository;

    @Test
    void testFindByUser_ShouldReturnGreaterThan0() {
        Long userId = 6L;
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(userLogoutLogRepository.findByUser(userId, pr).getTotalElements()).isGreaterThan(0);
    }

    @Test
    void testFindByUser_ShouldReturn0() {
        Long userId = 1L;
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(userLogoutLogRepository.findByUser(userId, pr).getTotalElements()).isEqualTo(0);
    }
}