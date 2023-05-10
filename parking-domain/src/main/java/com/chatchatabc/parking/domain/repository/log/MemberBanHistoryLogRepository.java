package com.chatchatabc.parking.domain.repository.log;

import com.chatchatabc.parking.domain.model.Member;
import com.chatchatabc.parking.domain.model.log.MemberBanHistoryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberBanHistoryLogRepository extends JpaRepository<MemberBanHistoryLog, String> {

    /**
     * Find the latest ban log for a member
     *
     * @param member the member
     * @return the latest ban log
     */
    @Query("SELECT m FROM MemberBanHistoryLog m WHERE m.member = ?1 AND m.status > -1 ORDER BY m.createdAt DESC LIMIT 1")
    Optional<MemberBanHistoryLog> findLatestBanLog(Member member);
}
