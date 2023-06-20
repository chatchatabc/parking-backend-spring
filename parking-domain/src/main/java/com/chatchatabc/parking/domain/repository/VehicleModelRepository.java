package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.VehicleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleModelRepository extends JpaRepository<VehicleModel, Long> {

    /**
     * Find Vehicle Model by Model UUID
     *
     * @param modelUuid Model UUID
     * @return Vehicle Model
     */
    Optional<VehicleModel> findByModelUuid(String modelUuid);

    /**
     * Find Vehicle Model by Brand UUID
     *
     * @param brandUuid Brand UUID
     * @param pageable  Pageable
     * @return Page of Vehicle Model
     */
    Page<VehicleModel> findAllByBrandUuid(String brandUuid, Pageable pageable);
}
