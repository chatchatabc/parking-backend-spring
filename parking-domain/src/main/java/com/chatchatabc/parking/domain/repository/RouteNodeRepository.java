package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.RouteNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RouteNodeRepository extends JpaRepository<RouteNode, Long> {

    /**
     * Find all route nodes by node id
     *
     * @param nodeIds node ids
     * @return list of route nodes
     */
    List<RouteNode> findAllByIdIn(Set<Long> nodeIds);


    /**
     * Find the closest node using Haversine formula
     *
     * @param latitude  latitude
     * @param longitude longitude
     * @param pageable  pageable
     * @return page of route nodes
     */
    @Query("""
            SELECT n
            FROM RouteNode n
            ORDER BY (6371 * acos(cos(radians(:latitude)) * cos(radians(n.latitude)) * cos(radians(n.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(n.latitude))))
            ASC
            """)
    Page<RouteNode> findClosestNode(double latitude, double longitude, Pageable pageable);
}
