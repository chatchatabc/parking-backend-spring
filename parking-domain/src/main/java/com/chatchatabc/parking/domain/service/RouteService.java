package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.model.Route;
import com.chatchatabc.parking.domain.model.RouteEdge;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RouteService {

    /**
     * Save route
     *
     * @param route route
     */
    Route saveRoute(Route route);

    /**
     * Find all paths
     *
     * @param edges    edges
     * @param start    start
     * @param end      end
     * @param maxDepth maxDepth
     * @return paths
     */
    List<List<Long>> findAllPaths(
            List<RouteEdge> edges,
            Long start,
            Long end,
            Integer maxDepth
    );

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
    void routeDFS(
            Map<Long, List<Long>> graph,
            Set<Long> visited,
            List<Long> stack,
            List<List<Long>> paths,
            Long currentNode,
            Long destination,
            Integer depth,
            Integer maxDepth
    );

    /**
     * Calculate cost of a ride
     *
     * @param distance distance
     * @return cost
     */
    // TODO: Add jeepney type
    Double calculateCost(Double distance);
}
