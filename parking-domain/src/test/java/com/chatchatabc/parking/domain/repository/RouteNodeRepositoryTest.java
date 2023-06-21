package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RouteNodeRepositoryTest extends TestContainersBaseTest {
    @Autowired
    private RouteNodeRepository routeNodeRepository;

    @Test
    void testFindAllByIdIn_ShouldReturnGreaterThan0() {
        Set<Long> nodeIds = new HashSet<>();
        nodeIds.add(1L);
        nodeIds.add(2L);
        assertThat(routeNodeRepository.findAllByIdIn(nodeIds).size()).isGreaterThan(0);
    }

    @Test
    void testFindAllByIdIn_ShouldReturn0() {
        Set<Long> nodeIds = new HashSet<>();
        nodeIds.add(0L);
        assertThat(routeNodeRepository.findAllByIdIn(nodeIds).size()).isEqualTo(0);
    }
}