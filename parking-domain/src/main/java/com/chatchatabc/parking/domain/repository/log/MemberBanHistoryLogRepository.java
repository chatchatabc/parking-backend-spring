package com.chatchatabc.parking.domain.repository.log;

import com.chatchatabc.parking.domain.model.log.MemberBanHistoryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberBanHistoryLogRepository extends JpaRepository<MemberBanHistoryLog, String> {
}
