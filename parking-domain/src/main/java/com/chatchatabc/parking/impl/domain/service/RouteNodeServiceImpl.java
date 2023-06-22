package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.RouteNode;
import com.chatchatabc.parking.domain.repository.RouteNodeRepository;
import com.chatchatabc.parking.domain.service.RouteNodeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteNodeServiceImpl implements RouteNodeService {
    private final RouteNodeRepository routeNodeRepository;

    public RouteNodeServiceImpl(RouteNodeRepository routeNodeRepository) {
        this.routeNodeRepository = routeNodeRepository;
    }

    /**
     * Save route node
     *
     * @param routeNode route node
     */
    @Override
    public void saveRouteNode(RouteNode routeNode) {
        routeNodeRepository.save(routeNode);
    }

    /**
     * Save route nodes
     *
     * @param routeNodes route nodes
     */
    @Override
    public void saveRouteNodes(List<RouteNode> routeNodes) {
        routeNodeRepository.saveAll(routeNodes);
    }
}
