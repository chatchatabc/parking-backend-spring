package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.enums.RoleNames;
import com.chatchatabc.parking.domain.model.Member;
import com.chatchatabc.parking.domain.model.Role;
import com.chatchatabc.parking.domain.repository.MemberRepository;
import com.chatchatabc.parking.domain.repository.RoleRepository;
import com.chatchatabc.parking.infra.service.KVService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MemberServiceImplTest extends TestContainersBaseTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private KVService kvService;

    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void softRegisterMember_NewMember_SuccessfullyRegistered() throws Exception {
        String phone = "1234567890";
        String username = "john_doe";
        when(memberRepository.findByPhone(phone)).thenReturn(Optional.empty());

        memberService.softRegisterMember(phone, username);

        verify(memberRepository, times(1)).findByPhone(phone);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void softRegisterMember_ExistingMemberWithCorrectUsername_NoExceptionThrown() throws Exception {
        String phone = "1234567890";
        String username = "john_doe";
        Member existingMember = new Member();
        existingMember.setUsername(username);
        when(memberRepository.findByPhone(phone)).thenReturn(Optional.of(existingMember));

        memberService.softRegisterMember(phone, username);

        verify(memberRepository, times(1)).findByPhone(phone);
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void softRegisterMember_ExistingMemberWithIncorrectUsername_ExceptionThrown() {
        String phone = "1234567890";
        String existingUsername = "john_doe";
        String incorrectUsername = "jane_smith";
        Member existingMember = new Member();
        existingMember.setUsername(existingUsername);
        when(memberRepository.findByPhone(phone)).thenReturn(Optional.of(existingMember));

        assertThrows(Exception.class, () ->
                memberService.softRegisterMember(phone, incorrectUsername));

        verify(memberRepository, times(1)).findByPhone(phone);
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void testVerifyOTPAndAddRole_WithInvalidOTP_ShouldThrowException() {
        String phone = "1234567890";
        String otp = "123456";
        String savedOTP = "654321";
        RoleNames roleName = RoleNames.ROLE_ADMIN;

        Member member = new Member();
        member.setPhone(phone);
        member.setRoles(new ArrayList<>());

        when(memberRepository.findByPhone(phone)).thenReturn(Optional.of(member));
        when(roleRepository.findByName(roleName.name())).thenReturn(Optional.of(new Role()));
        when(kvService.get("otp_" + phone)).thenReturn(savedOTP);

        assertThrows(Exception.class, () -> memberService.verifyOTPAndAddRole(phone, otp, roleName));
    }

    @Test
    void testVerifyOTPAndAddRole_WithExpiredOTP_ShouldThrowException() {
        // Arrange
        String phone = "1234567890";
        String otp = "123456";
        String savedOTP = "";
        RoleNames roleName = RoleNames.ROLE_ADMIN;

        Member member = new Member();
        member.setPhone(phone);
        member.setRoles(new ArrayList<>());

        when(memberRepository.findByPhone(phone)).thenReturn(Optional.of(member));
        when(roleRepository.findByName(roleName.name())).thenReturn(Optional.of(new Role()));
        when(kvService.get("otp_" + phone)).thenReturn(savedOTP);

        assertThrows(Exception.class, () -> memberService.verifyOTPAndAddRole(phone, otp, roleName));
    }

    @Test
    void testVerifyOTPAndAddRole_WithValidOTPAndExistingRole_ShouldNotAddRole() throws Exception {
        String phone = "1234567890";
        String otp = "123456";
        String savedOTP = "123456";

        Member member = new Member();
        member.setPhone(phone);
        Role memberRole = roleRepository.findByName(RoleNames.ROLE_MEMBER.name()).orElseThrow();
        member.getRoles().add(memberRole);

        when(memberRepository.findByPhone(phone)).thenReturn(Optional.of(member));
        when(roleRepository.findByName(RoleNames.ROLE_MEMBER.name())).thenReturn(Optional.of(memberRole));
        when(kvService.get("otp_" + phone)).thenReturn(savedOTP);

        Member result = memberService.verifyOTPAndAddRole(phone, otp, RoleNames.ROLE_MEMBER);

        verify(memberRepository, times(1)).findByPhone(phone);
        verify(roleRepository, times(1)).findByName(RoleNames.ROLE_MEMBER.name());
        verify(kvService, times(1)).get("otp_" + phone);
        verify(kvService, times(1)).delete("otp_" + phone);
        assertEquals(1, result.getRoles().size());
    }


    @Test
    void testVerifyOTPAndAddRole_WithValidOTPAndNewRole_ShouldAddRoleToMember() throws Exception {
        String phone = "1234567890";
        String otp = "123456";
        String savedOTP = "123456";
        String username = "john_doe";

        Member member = new Member();
        member.setPhone(phone);
        Role memberRole = roleRepository.findByName(RoleNames.ROLE_MEMBER.name()).orElseThrow();
        member.getRoles().add(memberRole);
        memberService.softRegisterMember(phone, username);

        when(memberRepository.findByPhone(phone)).thenReturn(Optional.of(member));
        when(roleRepository.findByName(RoleNames.ROLE_MEMBER.name())).thenReturn(Optional.of(memberRole));
        when(kvService.get("otp_" + phone)).thenReturn(savedOTP);

        Member result = memberService.verifyOTPAndAddRole(phone, otp, RoleNames.ROLE_MEMBER);

        verify(memberRepository, times(1)).findByPhone(phone);
        verify(roleRepository, times(1)).findByName(RoleNames.ROLE_MEMBER.name());
        verify(kvService, times(1)).get("otp_" + phone);
        verify(kvService, times(1)).delete("otp_" + phone);
        assertEquals(1, result.getRoles().size()); // One newly added role
    }
}
