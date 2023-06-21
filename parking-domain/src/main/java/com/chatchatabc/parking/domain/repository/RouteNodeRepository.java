package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.RouteNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteNodeRepository extends JpaRepository<RouteNode, Long> {
}
