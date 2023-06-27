package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.Route;
import com.chatchatabc.parking.domain.model.RouteEdge;
import com.chatchatabc.parking.domain.repository.RouteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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
        route.setColor("Red");
        route.setStatus(Route.RouteStatus.DRAFT);

        routeService.saveRoute(route);
        assertThat(routeRepository.count()).isGreaterThan(count);
    }

    @Test
    void testFindAllPaths_ShouldBeSuccessful() {
        Long start = 1L;
        Long end = 2L;
        Integer maxDepth = 10;

        RouteEdge edge = new RouteEdge();
        edge.setNodeFrom(1L);
        edge.setNodeTo(2L);
        edge.setDistance(10.0);

        RouteEdge edge2 = new RouteEdge();
        edge2.setNodeFrom(2L);
        edge2.setNodeTo(3L);
        edge2.setDistance(10.0);

        List<RouteEdge> edges = List.of(edge, edge2);

        List<List<Long>> paths = routeService.findAllPaths(edges, start, end, maxDepth);
        assertThat(paths.size()).isEqualTo(1);
        assertThat(paths.get(0).size()).isEqualTo(2);
        assertThat(paths.get(0)).isEqualTo(List.of(1L, 2L));
    }

    @Test
    void testRouteDFS_ShouldBeSuccessful() {
        Long start = 1L;
        Long end = 3L;
        Integer maxDepth = 10;

        RouteEdge edge1 = new RouteEdge();
        edge1.setNodeFrom(1L);
        edge1.setNodeTo(2L);
        edge1.setDistance(10.0);

        RouteEdge edge2 = new RouteEdge();
        edge2.setNodeFrom(2L);
        edge2.setNodeTo(3L);
        edge2.setDistance(10.0);

        List<RouteEdge> edges = List.of(edge1, edge2);

        // Create graph
        Map<Long, List<Long>> graph = new HashMap<>();
        for (RouteEdge edge : edges) {
            graph.computeIfAbsent(edge.getNodeFrom(), k -> new ArrayList<>()).add(edge.getNodeTo());
        }

        // Depth First Search
        Set<Long> visited = new HashSet<>();
        List<Long> stack = new ArrayList<>();
        stack.add(start);
        List<List<Long>> paths = new ArrayList<>();

        routeService.routeDFS(graph, visited, stack, paths, start, end, 1, maxDepth);
    }

    @Test
    void testCalculateCost_WhenDistanceIs10_ShouldReturnSuccessfully() {
        Double distance = 10.0;
        assertThat(routeService.calculateCost(distance)).isEqualTo(22.8);
    }
}