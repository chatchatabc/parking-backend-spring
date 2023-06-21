package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.VehicleBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleBrandRepository extends JpaRepository<VehicleBrand, Long>, JpaSpecificationExecutor<VehicleBrand> {

    /**
     * Find Vehicle Brand by Brand UUID
     *
     * @param brandUuid Brand UUID
     * @return Vehicle Brand
     */
    Optional<VehicleBrand> findByBrandUuid(String brandUuid);
}
