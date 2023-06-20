package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long>, JpaSpecificationExecutor<VehicleType> {

    /**
     * Find Vehicle Type by Type UUID
     *
     * @param typeUuid Type UUID
     * @return Vehicle Type
     */
    Optional<VehicleType> findByTypeUuid(String typeUuid);
}
