package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.RouteEdge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteEdgeRepository extends JpaRepository<RouteEdge, Long> {
}
