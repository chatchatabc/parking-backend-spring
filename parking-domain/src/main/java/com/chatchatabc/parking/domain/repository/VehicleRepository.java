package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    /**
     * Find a vehicle by its uuid and owner id
     */
    @Query("SELECT v FROM Vehicle v WHERE v.vehicleUuid = ?1 AND v.owner = ?2")
    Optional<Vehicle> findByVehicleUuidAndOwner(String vehicleUuid, Long owner);

    /**
     * Find a vehicle by its uuid
     *
     * @param vehicleUuid the vehicle uuid
     * @return the vehicle
     */
    Optional<Vehicle> findByVehicleUuid(String vehicleUuid);

    /**
     * Find all vehicles of an owner
     */
    @Query("SELECT v FROM Vehicle v WHERE v.owner = ?1")
    Page<Vehicle> findAllByOwner(Long owner, Pageable pageable);

    /**
     * Find all vehicles of an owner by user uuid
     *
     * @param userUuid the user uuid
     * @param pageable the pageable
     * @return the page
     */
    @Query("SELECT v FROM Vehicle v JOIN v.users u WHERE u.userUuid = :userUuid")
    Page<Vehicle> findAllByUser(String userUuid, Pageable pageable);

    /**
     * Find a vehicle by its plate number
     *
     * @param plateNumber the plate number
     * @return the vehicle
     */
    Optional<Vehicle> findByPlateNumber(String plateNumber);

    /**
     * Find all vehicles ids by parking lot
     *
     * @param owner the owner
     * @return the list of vehicle ids
     */
    @Query("SELECT v.id FROM Vehicle v WHERE v.owner = ?1")
    List<Integer> findVehicleIdsByOwner(Long owner);
}
