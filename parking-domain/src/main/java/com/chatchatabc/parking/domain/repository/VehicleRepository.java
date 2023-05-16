package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.Member;
import com.chatchatabc.parking.domain.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    /**
     * Find a vehicle by its id and owner
     */
    @Query("SELECT v FROM Vehicle v WHERE v.id = ?1 AND v.owner = ?2")
    Optional<Vehicle> findByIdAndOwner(String vehicleId, Member owner);

    /**
     * Find a vehicle by its uuid
     *
     * @param vehicleUuid the vehicle uuid
     * @return the vehicle
     */
    Optional<Vehicle> findByVehicleUuid(String vehicleUuid);

    /**
     * Find all vehicles of a owner
     */
    @Query("SELECT v FROM Vehicle v WHERE v.owner = ?1")
    Page<Vehicle> findAllByOwner(Member owner, Pageable pageable);

    /**
     * Find all vehicles of an owner by member id
     *
     * @param memberId the member id
     * @param pageable the pageable
     * @return the page
     */
    @Query("SELECT v FROM Vehicle v JOIN v.members u WHERE u.id = :memberId")
    Page<Vehicle> findAllByMember(String memberId, Pageable pageable);
}
