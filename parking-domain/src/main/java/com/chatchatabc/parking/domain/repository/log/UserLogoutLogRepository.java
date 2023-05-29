package com.chatchatabc.parking.domain.repository.log;

import com.chatchatabc.parking.domain.model.log.UserLogoutLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogoutLogRepository extends JpaRepository<UserLogoutLog, String> {
    /**
     * Find all user logout logs by user
     *
     * @param userId   the user id
     * @param pageable the pageable
     * @return the Page<UserLogoutLog>
     */
    @Query("SELECT l FROM UserLogoutLog l WHERE l.user = ?1")
    Page<UserLogoutLog> findByUser(Long userId, Pageable pageable);
}
