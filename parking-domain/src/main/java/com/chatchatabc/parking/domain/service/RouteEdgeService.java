package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.model.RouteEdge;
import com.chatchatabc.parking.domain.model.RouteNode;

import java.util.List;

public interface RouteEdgeService {

    /**
     * Save route edge
     *
     * @param routeEdge route edge
     */
    void saveRouteEdge(RouteEdge routeEdge);

    /**
     * Save route edges
     *
     * @param routeEdges route edges
     */
    void saveRouteEdges(List<RouteEdge> routeEdges);

    /**
     * Delete route edges by ids
     *
     * @param ids ids
     */
    void deleteRouteEdges(List<Long> ids);

    /**
     * Calculate distance between two nodes
     *
     * @param nodeFrom node from
     * @param nodeTo   node to
     * @return distance
     */
    Double calculateDistanceBetweenNodes(RouteNode nodeFrom, RouteNode nodeTo);

    /**
     * Calculate radians from degree
     *
     * @param degree degree
     * @return radians
     */
    Double toRadians(Double degree);
}
