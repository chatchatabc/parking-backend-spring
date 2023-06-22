package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.RouteEdge;
import com.chatchatabc.parking.domain.model.RouteNode;
import com.chatchatabc.parking.domain.repository.RouteEdgeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RouteEdgeServiceImplTest extends TestContainersBaseTest {
    @Autowired
    private RouteEdgeServiceImpl routeEdgeService;

    @Autowired
    private RouteEdgeRepository routeEdgeRepository;

    @Test
    void testSaveRouteEdge_ShouldBeSuccessful() {
        // Given
        RouteEdge routeEdge = new RouteEdge();
        routeEdge.setRouteId(1L);
        routeEdge.setNodeTo(1L);
        routeEdge.setNodeFrom(2L);
        routeEdge.setDistance(1.0);
        Long count = routeEdgeRepository.count();

        // When
        routeEdgeService.saveRouteEdge(routeEdge);

        // Then
        assertThat(routeEdgeRepository.count()).isGreaterThan(count);
    }

    @Test
    void testSaveRouteEdges_ShouldBeSuccessful() {
        // Given
        RouteEdge routeEdge = new RouteEdge();
        routeEdge.setRouteId(1L);
        routeEdge.setNodeTo(1L);
        routeEdge.setNodeFrom(2L);
        routeEdge.setDistance(1.0);

        RouteEdge routeEdge2 = new RouteEdge();
        routeEdge2.setRouteId(1L);
        routeEdge2.setNodeTo(2L);
        routeEdge2.setNodeFrom(3L);
        routeEdge2.setDistance(2.0);

        Long count = routeEdgeRepository.count();

        // When
        routeEdgeService.saveRouteEdges(List.of(routeEdge, routeEdge2));

        // Then
        assertThat(routeEdgeRepository.count()).isGreaterThan(count);

    }

    @Test
    void testCalculateDistanceBetweenNodes_ShouldBeSuccessful() {
        // Given
        RouteNode nodeFrom = new RouteNode();
        nodeFrom.setLatitude(1.0);
        nodeFrom.setLongitude(1.0);
        RouteNode nodeTo = new RouteNode();
        nodeTo.setLatitude(2.0);
        nodeTo.setLongitude(2.0);

        // When
        Double distance = routeEdgeService.calculateDistanceBetweenNodes(nodeFrom, nodeTo);

        // Then
        assertThat(distance).isEqualTo(157225.4320380729);
    }

    @Test
    void testToRadians_ShouldBeSuccessful() {
        Double degree = 1.0;
        assertThat(routeEdgeService.toRadians(degree)).isEqualTo(0.017453292519943295);
    }
}