package com.chatchatabc.parking.domain.repository.log;

import com.chatchatabc.parking.domain.model.log.UserBanHistoryLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserBanHistoryLogRepository extends JpaRepository<UserBanHistoryLog, String> {

    /**
     * Find the latest ban log for a user
     *
     * @param userId the id of the user
     * @return the latest ban log
     */
    @Query("SELECT m FROM UserBanHistoryLog m WHERE m.user = ?1 AND m.status > -1 ORDER BY m.createdAt DESC LIMIT 1")
    Optional<UserBanHistoryLog> findLatestBanLog(Long userId);

    /**
     * Find all ban logs for a user
     *
     * @param userId   the id of the user
     * @param pageable the pageable
     * @return the page of ban logs
     */
    Page<UserBanHistoryLog> findAllByUser(Long userId, Pageable pageable);
}
