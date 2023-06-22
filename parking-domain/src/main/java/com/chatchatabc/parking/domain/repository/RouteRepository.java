package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long>, JpaSpecificationExecutor<Route> {

    /**
     * Find route by route uuid
     *
     * @param routeUuid route uuid
     * @return route
     */
    Optional<Route> findByRouteUuid(String routeUuid);

    /**
     * Find route by slug
     *
     * @param slug slug
     * @return route
     */
    Optional<Route> findBySlug(String slug);
}
