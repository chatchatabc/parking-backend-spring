package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.RouteEdge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteEdgeRepository extends JpaRepository<RouteEdge, Long> {

    /**
     * Find all route edges by route id
     *
     * @param routeId route id
     * @return list of route edges
     */
    List<RouteEdge> findAllByRouteId(Long routeId);
}
