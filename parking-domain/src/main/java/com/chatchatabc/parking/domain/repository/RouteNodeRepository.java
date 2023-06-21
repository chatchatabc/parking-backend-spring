package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.RouteNode;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
