package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.VehicleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleModelRepository extends JpaRepository<VehicleModel, Long>, JpaSpecificationExecutor<VehicleModel> {

    /**
     * Find Vehicle Model by Status
     *
     * @param status   Vehicle Model Status
     * @param pageable Pageable
     * @return Page of Vehicle Model
     */
    Page<VehicleModel> findAllByStatus(Integer status, Pageable pageable);

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

    /**
     * Find Vehicle Model by Brand UUID and Type UUID
     *
     * @param typeUuid  Type UUID
     * @param brandUuid Brand UUID
     * @param pageable  Pageable
     * @return Page of Vehicle Model
     */
    Page<VehicleModel> findAllByTypeUuidAndBrandUuid(String typeUuid, String brandUuid, Pageable pageable);

    /**
     * Find Vehicle Model by Brand UUID and Type UUID and Status
     *
     * @param typeUuid  the type uuid
     * @param brandUuid the brand uuid
     * @param status    the status
     * @param pageable  the pageable
     * @return Page of Vehicle Model
     */
    Page<VehicleModel> findAllByTypeUuidAndBrandUuidAndStatus(String typeUuid, String brandUuid, Integer status, Pageable pageable);
}
