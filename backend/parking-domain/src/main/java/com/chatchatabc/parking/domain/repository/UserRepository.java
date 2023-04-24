package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

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
}
