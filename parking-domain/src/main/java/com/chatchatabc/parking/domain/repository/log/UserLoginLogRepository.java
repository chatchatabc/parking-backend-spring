package com.chatchatabc.parking.domain.repository.log;

import com.chatchatabc.parking.domain.model.User;
import com.chatchatabc.parking.domain.model.log.UserLoginLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginLogRepository extends JpaRepository<UserLoginLog, String> {

    /**
     * Find all user login logs by user
     *
     * @param user     the user
     * @param pageable the pageable
     * @return the Page<UserLoginLog>
     */
    @Query("SELECT l FROM UserLoginLog l WHERE l.user = ?1")
    Page<UserLoginLog> findByUser(User user, Pageable pageable);
}
