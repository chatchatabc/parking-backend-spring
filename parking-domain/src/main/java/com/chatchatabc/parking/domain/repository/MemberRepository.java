package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {

    /**
     * Find member by username
     *
     * @param username the username
     * @return the member
     */
    Optional<Member> findByUsername(String username);

    /**
     * Find member by phone
     *
     * @param phone the phone
     * @return the member
     */
    Optional<Member> findByPhone(String phone);

    /**
     * Count members by role name
     *
     * @param roleName the role name
     * @return the count
     */
    @Query("SELECT COUNT(u.id) FROM Member u JOIN u.roles r WHERE r.name = :roleName")
    Long countMembersByRole(String roleName);


    /**
     * Find member by member id
     *
     * @param memberId the member id
     * @return the member
     */
    Optional<Member> findByMemberId(String memberId);

    /**
     * Count verified members
     *
     * @return the count
     */
    @Query("SELECT COUNT(u.id) FROM Member u WHERE u.emailVerifiedAt IS NOT NULL OR u.phoneVerifiedAt IS NOT NULL")
    Long countVerified();
}
