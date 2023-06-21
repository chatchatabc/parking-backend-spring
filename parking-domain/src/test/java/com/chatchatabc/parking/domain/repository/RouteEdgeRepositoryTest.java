package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RouteEdgeRepositoryTest extends TestContainersBaseTest {
    @Autowired
    private RouteEdgeRepository routeEdgeRepository;

    @Test
    void testFindAllByRouteId_ShouldReturnGreaterThan0() {
        Long routeId = 1L;
        assertThat(routeEdgeRepository.findAllByRouteId(routeId).size()).isGreaterThan(0);
    }

    @Test
    void testFindAllByRouteId_ShouldReturn0() {
        Long routeId = 0L;
        assertThat(routeEdgeRepository.findAllByRouteId(routeId).size()).isEqualTo(0);
    }
}