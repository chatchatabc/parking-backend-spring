package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DataSet({"db/datasets/member.xml", "db/datasets/role.xml"})
class MemberRepositoryTest extends TestContainersBaseTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void findByUsername() {
    }

    @Test
    void findByPhone() {
    }

    @Test
    void countMembersByRole() {
    }

    @Test
    void findByMemberUuid() {
    }

    @Test
    void countVerified() {
    }
}