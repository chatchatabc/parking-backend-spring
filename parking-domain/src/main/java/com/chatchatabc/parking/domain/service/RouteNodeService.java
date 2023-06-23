package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.model.RouteNode;

import java.util.List;

public interface RouteNodeService {

    /**
     * Save route node
     *
     * @param routeNode route node
     */
    void saveRouteNode(RouteNode routeNode);

    /**
     * Save route nodes
     *
     * @param routeNodes route nodes
     */
    void saveRouteNodes(List<RouteNode> routeNodes);
}
