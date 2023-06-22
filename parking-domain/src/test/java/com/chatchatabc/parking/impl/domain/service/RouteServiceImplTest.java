package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.Route;
import com.chatchatabc.parking.domain.repository.RouteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RouteServiceImplTest extends TestContainersBaseTest {
    @Autowired
    private RouteServiceImpl routeService;
    @Autowired
    private RouteRepository routeRepository;

    @Test
    void testSaveRoute_ShouldBeSuccessful() {
        Long count = routeRepository.count();

        Route route = new Route();
        route.setName("route-name");
        route.setDescription("description");
        route.setSlug("route-slug");
        route.setStatus(Route.RouteStatus.DRAFT);

        routeService.saveRoute(route);
        assertThat(routeRepository.count()).isGreaterThan(count);
    }
}