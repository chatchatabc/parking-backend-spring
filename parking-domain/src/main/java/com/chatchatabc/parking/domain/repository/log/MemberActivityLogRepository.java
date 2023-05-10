package com.chatchatabc.parking.domain.repository.log;

import com.chatchatabc.parking.domain.model.log.MemberActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberActivityLogRepository extends JpaRepository<MemberActivityLog, String> {
}
