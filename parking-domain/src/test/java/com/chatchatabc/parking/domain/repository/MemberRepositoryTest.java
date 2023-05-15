package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.Member;
import com.github.database.rider.core.api.dataset.DataSet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataSet("db/datasets/member.xml")
class MemberRepositoryTest extends TestContainersBaseTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void testFindByUsername_UsernameIsFound() {
        String username = "admin";
        Optional<Member> member = memberRepository.findByUsername(username);
        assertThat(member).isPresent();
        assertThat(member.get().getUsername()).isEqualTo(username);
    }

    @Test
    void testFindByUsername_UsernameNotFound() {
        String username = "nonexistent";
        Optional<Member> member = memberRepository.findByUsername(username);
        assertThat(member).isNotPresent();
    }

    @Test
    void testFindByPhone_PhoneIsFound() {
        String phone = "1234567890";
        Optional<Member> member = memberRepository.findByPhone(phone);
        assertThat(member).isPresent();
        assertThat(member.get().getPhone()).isEqualTo(phone);
    }

    @Test
    void testFindByPhone_PhoneNotFound() {
        String phone = "9999999999";
        Optional<Member> member = memberRepository.findByPhone(phone);
        assertThat(member).isNotPresent();
    }

    @Test
    void testCountMembersByRole() {
        String roleName = "ROLE_ADMIN";
        Long count = memberRepository.countMembersByRole(roleName);
        assertThat(count).isEqualTo(1L);
    }

    @Test
    void testFindByMemberUuid_UuidIsFound() {
        String memberUuid = "ec4af6e9-ec57-434d-990d-ae83d9459a31";
        Optional<Member> member = memberRepository.findByMemberUuid(memberUuid);
        assertThat(member).isPresent();
        assertThat(member.get().getMemberUuid()).isEqualTo(memberUuid);
    }

    @Test
    void testFindByMemberUuid_UuidNotFound() {
        String memberUuid = "00000000-0000-0000-0000-000000000000";
        Optional<Member> member = memberRepository.findByMemberUuid(memberUuid);
        assertThat(member).isNotPresent();
    }


    @Test
    void testCountVerified() {
        Long count = memberRepository.countVerified();
        assertThat(count).isEqualTo(4L);
    }
}