package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.enums.RoleNames;
import com.chatchatabc.parking.domain.repository.MemberRepository;
import com.chatchatabc.parking.infra.service.KVService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberServiceImplTest extends TestContainersBaseTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private KVService kvService;
    @Autowired
    private MemberServiceImpl memberService;

    @Test
    void testSoftRegisterMember_ShouldSuccessfullyRegister() throws Exception {
        String phone = "69420961111";
        String username = "soft_register";

        assertThat(memberRepository.findByPhone(phone)).isEmpty();

        memberService.softRegisterMember(phone, username);

        assertThat(memberRepository.findByPhone(phone)).isPresent();
    }

    @Test
    void testSoftRegisterMember_ExistingMemberWithCorrectUsername_ShouldSuccessfullyRegister() throws Exception {
        String phone = "1234567890";
        String username = "admin";

        assertThat(memberRepository.findByPhone(phone)).isPresent();

        memberService.softRegisterMember(phone, username);

        assertThat(memberRepository.findByPhone(phone)).isPresent();
    }

    @Test
    void testSoftRegisterMember_ExistingMemberWithIncorrectUsername_ExceptionThrown() throws Exception {
        String phone = "1234567890";
        String username = "adminWrong";

        assertThat(memberRepository.findByPhone(phone)).isPresent();

        assertThrows(Exception.class, () -> memberService.softRegisterMember(phone, username));
    }

    @Test
    void testVerifyOTPAndAddRole_WithValidOTP_ShouldSuccessfullyVerify() throws Exception {
        String phone = "1234567890";
        String otp = "123456";
        RoleNames roleName = RoleNames.ROLE_ADMIN;

        // Set otp to KV Service
        kvService.set("otp_" + phone, otp, 900L);

        assertThat(memberService.verifyOTPAndAddRole(phone, otp, roleName)).isNotNull();
    }

    @Test
    void testVerifyOTPAndAddRole_WithInvalidOTP_ShouldThrowException() throws Exception {
        String phone = "9906345322";
        String otp = "123456";
        String invalidOtp = "654321";

        RoleNames roleName = RoleNames.ROLE_ADMIN;

        // Set otp to KV Service
        kvService.set("otp_" + phone, otp, 900L);

        assertThrows(Exception.class, () -> memberService.verifyOTPAndAddRole(phone, invalidOtp, roleName));
    }

    @Test
    void testVerifyOTPAndAddRole_WithExpiredOTP_ShouldThrowException() {
        String phone = "7688453415";
        String otp = "123456";

        RoleNames roleName = RoleNames.ROLE_ADMIN;

        // OTP not saved to KV since expired OTPs are deleted
        assertThrows(Exception.class, () -> memberService.verifyOTPAndAddRole(phone, otp, roleName));
    }
}
