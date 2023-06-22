package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.RouteNode;
import com.chatchatabc.parking.domain.repository.RouteNodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RouteNodeServiceImplTest extends TestContainersBaseTest {
    @Autowired
    private RouteNodeRepository routeNodeRepository;

    @Autowired
    private RouteNodeServiceImpl routeNodeService;

    @Test
    void testSaveRouteNodes_ShouldBeSuccessful() {
        // Given
        RouteNode routeNode = new RouteNode();
        routeNode.setLatitude(1.0);
        routeNode.setLongitude(1.0);
        routeNode.setPoi("POI");

        RouteNode routeNode2 = new RouteNode();
        routeNode2.setLatitude(2.0);
        routeNode2.setLongitude(2.0);
        routeNode2.setPoi("POI2");

        List<RouteNode> routeNodes = List.of(routeNode, routeNode2);
        Long count = routeNodeRepository.count();

        // When
        routeNodeService.saveRouteNodes(routeNodes);

        // Then
        assertThat(routeNodeRepository.count()).isGreaterThan(count);
    }
}