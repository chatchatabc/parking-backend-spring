package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.RouteEdge;
import com.chatchatabc.parking.domain.model.RouteNode;
import com.chatchatabc.parking.domain.repository.RouteEdgeRepository;
import com.chatchatabc.parking.domain.service.RouteEdgeService;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Math.*;

@Service
public class RouteEdgeServiceImpl implements RouteEdgeService {
    private final RouteEdgeRepository routeEdgeRepository;

    public RouteEdgeServiceImpl(RouteEdgeRepository routeEdgeRepository) {
        this.routeEdgeRepository = routeEdgeRepository;
    }

    /**
     * Save route edge
     *
     * @param routeEdge route edge
     */
    @Override
    public void saveRouteEdge(RouteEdge routeEdge) {
        routeEdgeRepository.save(routeEdge);
    }

    /**
     * Save route edges
     *
     * @param routeEdges route edges
     */
    @Override
    public void saveRouteEdges(List<RouteEdge> routeEdges) {
        routeEdgeRepository.saveAll(routeEdges);
    }

    /**
     * Delete route edges by ids
     *
     * @param ids ids
     */
    @Override
    public void deleteRouteEdges(List<Long> ids) {
        routeEdgeRepository.deleteAllById(ids);
    }

    /**
     * Calculate distance between two nodes
     *
     * @param nodeFrom node from
     * @param nodeTo   node to
     * @return distance
     */
    @Override
    public Double calculateDistanceBetweenNodes(RouteNode nodeFrom, RouteNode nodeTo) {
        Integer r = 6371000; // Radius of the earth in meters
        Double dLat = toRadians(nodeTo.getLatitude() - nodeFrom.getLatitude());
        Double dLon = toRadians(nodeTo.getLongitude() - nodeFrom.getLongitude());
        double a = sin(dLat / 2) * sin(dLat / 2) +
                cos(toRadians(nodeFrom.getLatitude())) *
                        cos(toRadians(nodeTo.getLatitude())) *
                        sin(dLon / 2) *
                        sin(dLon / 2);
        Double c = 2 * atan2(sqrt(a), sqrt(1 - a));
        return r * c; // Distance in meters
    }

    /**
     * Calculate radians from degree
     *
     * @param degree degree
     * @return radians
     */
    @Override
    public Double toRadians(Double degree) {
        return degree * (Math.PI / 180.0);
    }
}
