package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.User;
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
     * Find a vehicle by its id and user
     */
    @Query("SELECT v FROM Vehicle v WHERE v.id = ?1 AND v.owner = ?2")
    Optional<Vehicle> findByIdAndOwner(String vehicleId, User owner);

    /**
     * Find all vehicles of a user
     */
    @Query("SELECT v FROM Vehicle v WHERE v.owner = ?1")
    Page<Vehicle> findAllByOwner(User owner, Pageable pageable);

    /**
     * Find all vehicles of a user by user id
     *
     * @param userId   the user id
     * @param pageable the pageable
     * @return the page
     */
    @Query("SELECT v FROM Vehicle v JOIN v.users u WHERE u.id = :userId")
    Page<Vehicle> findAllByUser(String userId, Pageable pageable);
}
