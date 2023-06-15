package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RouteRepositoryTest extends TestContainersBaseTest {
    @Autowired
    private RouteRepository routeRepository;

    @Test
    void testFindByRouteUuid_WhenRouteUuidExists_ShouldReturnRoute() {
        String routeUuid = "8821fff6-b725-454c-bdd3-0674b313ba45";
        assertThat(routeRepository.findByRouteUuid(routeUuid)).isPresent();
    }

    @Test
    void testFindByRouteUuid_WhenRouteUuidDoesNotExist_ShouldReturnEmpty() {
        String routeUuid = "non-existent-route-uuid";
        assertThat(routeRepository.findByRouteUuid(routeUuid)).isEmpty();
    }
}