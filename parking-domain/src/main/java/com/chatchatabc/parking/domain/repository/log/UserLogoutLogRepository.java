package com.chatchatabc.parking.domain.repository.log;

import com.chatchatabc.parking.domain.model.log.UserLogoutLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogoutLogRepository extends JpaRepository<UserLogoutLog, String> {
}
