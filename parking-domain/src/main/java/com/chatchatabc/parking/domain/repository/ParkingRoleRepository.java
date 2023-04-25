package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.ParkingRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingRoleRepository extends JpaRepository<ParkingRole, String> {

    /**
     * Find role by name
     *
     * @param name the name
     * @return the role
     */
    Optional<ParkingRole> findByName(String name);
}
