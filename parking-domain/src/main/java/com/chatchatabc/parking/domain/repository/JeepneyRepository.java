package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.Jeepney;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JeepneyRepository extends JpaRepository<Jeepney, Long>, JpaSpecificationExecutor<Jeepney> {

    /**
     * Find jeepney by jeepney uuid
     *
     * @param jeepneyUuid jeepney uuid
     * @return jeepney
     */
    Optional<Jeepney> findByJeepneyUuid(String jeepneyUuid);

    /**
     * Find jeepney by plate number
     *
     * @param plateNumber plate number
     * @return jeepney
     */
    Optional<Jeepney> findByPlateNumber(String plateNumber);

    /**
     * Find jeepney by name
     *
     * @param name name
     * @return jeepney
     */
    Optional<Jeepney> findByName(String name);

    /**
     * Find all jeepneys by route uuid
     *
     * @param routeUuid route uuid
     * @return list of jeepneys
     */
    // TODO: Make unit test for this
    Page<Jeepney> findAllByRouteUuid(String routeUuid, Pageable pageable);
}
