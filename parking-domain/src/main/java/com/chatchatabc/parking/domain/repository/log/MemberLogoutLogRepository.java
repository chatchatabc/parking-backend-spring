package com.chatchatabc.parking.domain.repository.log;

import com.chatchatabc.parking.domain.model.Member;
import com.chatchatabc.parking.domain.model.log.MemberLogoutLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberLogoutLogRepository extends JpaRepository<MemberLogoutLog, String> {
    /**
     * Find all member login logs by member
     *
     * @param member   the member
     * @param pageable the pageable
     * @return the Page<MemberLoginLog>
     */
    @Query("SELECT l FROM MemberLoginLog l WHERE l.member = ?1")
    Page<MemberLogoutLog> findByMember(Member member, Pageable pageable);
}
