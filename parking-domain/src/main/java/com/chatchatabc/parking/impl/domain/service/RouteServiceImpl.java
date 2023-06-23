package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.Route;
import com.chatchatabc.parking.domain.model.RouteEdge;
import com.chatchatabc.parking.domain.repository.RouteRepository;
import com.chatchatabc.parking.domain.service.RouteService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouteServiceImpl implements RouteService {
    private final RouteRepository routeRepository;

    public RouteServiceImpl(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    /**
     * Save route
     *
     * @param route route
     */
    @Override
    public Route saveRoute(Route route) {
        return routeRepository.save(route);
    }

    /**
     * Find all paths
     *
     * @param edges    edges
     * @param start    start
     * @param end      end
     * @param maxDepth maxDepth
     * @return paths
     */
    @Override
    public List<List<Long>> findAllPaths(List<RouteEdge> edges, Long start, Long end, Integer maxDepth) {
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
        routeDFS(graph, visited, stack, paths, start, end, 1, maxDepth);

        return paths;
    }

    /**
     * Calculate route paths
     *
     * @param graph       graph
     * @param visited     visited
     * @param stack       stack
     * @param paths       paths
     * @param currentNode currentNode
     * @param destination destination
     * @param depth       depth
     * @param maxDepth    maxDepth
     */
    @Override
    public void routeDFS(Map<Long, List<Long>> graph, Set<Long> visited, List<Long> stack, List<List<Long>> paths, Long currentNode, Long destination, Integer depth, Integer maxDepth) {
        // Limit the search depth
        if (depth > maxDepth) return;

        // Add current node to stack
        if (Objects.equals(currentNode, destination)) paths.add(stack.stream().toList());

            // Else, add current node to visited
        else {
            visited.add(currentNode);
            // For each node in the graph
            for (Long neighbor : graph.getOrDefault(currentNode, List.of())) {
                // If neighbor is not visited
                if (!visited.contains(neighbor)) {
                    // Add neighbor to stack
                    stack.add(neighbor);
                    // Recursively call routeDFS
                    routeDFS(graph, visited, stack, paths, neighbor, destination, depth + 1, maxDepth);
                    // Remove neighbor from stack
                    stack.remove(stack.size() - 1); // Remove last element
                }
            }
            // Remove current node from visited
            visited.remove(currentNode);
        }
    }

    /**
     * Calculate cost of a ride
     *
     * @param distance distance
     * @return cost
     */
    @Override
    public Double calculateCost(Double distance) {
        // Distance | Fare
        // 0 - 4 km | PHP 12
        // 4 - 5 km | PHP 13.80
        // 5 - 6 km | PHP 15.60
        // 6 - 7 km | PHP 17.40
        // 7 - 8 km | PHP 19.20
        // 8 - 9 km | PHP 21.00
        // 9 - 10 km | PHP 22.80

        // TODO: Add jeepney types
        // Filipino jeepney fare is 1.80 pesos per 1 km. But the first 4 km is 12 pesos
        // distance is in meters
        double first4Km = 12.0;
        double after4Km = 1.80;
        if (distance <= 4) {
            return first4Km;
        }
        return first4Km + ((distance - 4) * after4Km);
    }
}
