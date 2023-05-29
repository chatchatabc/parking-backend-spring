package com.chatchatabc.parking.domain.repository.log;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

class UserLoginLogRepositoryTest extends TestContainersBaseTest {
    @Autowired
    private UserLoginLogRepository userLoginLogRepository;

    @Test
    void testFindByUser_ShouldReturnGreaterThan0() {
        Long userId = 6L;
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(userLoginLogRepository.findByUser(userId, pr).getTotalElements()).isGreaterThan(0L);
    }

    @Test
    void testFindByUser_ShouldReturn0() {
        Long userId = 5L;
        PageRequest pr = PageRequest.of(0, 10);
        assertThat(userLoginLogRepository.findByUser(userId, pr).getTotalElements()).isEqualTo(0L);
    }
}