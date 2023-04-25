package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    /**
     * Find role by name
     *
     * @param name the role name
     * @return the role
     */
    Optional<Role> findByName(String name);
}
