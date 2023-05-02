package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Find user by username
     *
     * @param username the username
     * @return the user
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by phone
     *
     * @param phone the phone
     * @return the user
     */
    Optional<User> findByPhone(String phone);

    /**
     * Count users by role name
     *
     * @param roleName the role name
     * @return the count
     */
    @Query("SELECT COUNT(u.id) FROM User u JOIN u.roles r WHERE r.name = :roleName")
    Long countUsersByRole(String roleName);


    /**
     * Find user by user id
     *
     * @param userId the user id
     * @return the user
     */
    Optional<User> findByUserId(String userId);
}
