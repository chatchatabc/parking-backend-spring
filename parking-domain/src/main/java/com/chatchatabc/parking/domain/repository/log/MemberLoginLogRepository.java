package com.chatchatabc.parking.domain.repository.log;

import com.chatchatabc.parking.domain.model.Member;
import com.chatchatabc.parking.domain.model.log.MemberLoginLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberLoginLogRepository extends JpaRepository<MemberLoginLog, String> {

    /**
     * Find all member login logs by member
     *
     * @param member     the user
     * @param pageable the pageable
     * @return the Page<UserLoginLog>
     */
    @Query("SELECT l FROM MemberLoginLog l WHERE l.member = ?1")
    Page<MemberLoginLog> findByMember(Member member, Pageable pageable);
}
