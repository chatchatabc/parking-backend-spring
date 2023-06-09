package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {
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
    @Query("SELECT v.vehicleUuid FROM Vehicle v WHERE v.owner = ?1")
    List<String> findVehicleIdsByOwner(Long owner);

    /**
     * Find all vehicles by parking lot uuid and keyword and date range through invoices
     *
     * @param keyword  the keyword
     * @param pageable the pageable
     * @return the page
     */
    @Query("SELECT DISTINCT v FROM Vehicle v INNER JOIN Invoice i ON v.vehicleUuid = i.vehicleUuid INNER JOIN ParkingLot pl ON i.parkingLotUuid = pl.parkingLotUuid WHERE pl.parkingLotUuid = ?1 AND LOWER(v.plateNumber) LIKE %?2% AND i.createdAt BETWEEN ?3 AND ?4")
    Page<Vehicle> findAllVehiclesByParkingLotUuidAndKeywordAndDateRangeThroughInvoices(String parkingLotUuid, String keyword, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
