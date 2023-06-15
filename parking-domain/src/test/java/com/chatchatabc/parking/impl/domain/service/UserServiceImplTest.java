package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.Role;
import com.chatchatabc.parking.domain.model.User;
import com.chatchatabc.parking.domain.repository.UserRepository;
import com.chatchatabc.parking.infra.service.KVService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceImplTest extends TestContainersBaseTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KVService kvService;
    @Autowired
    private UserServiceImpl userService;

    @Test
    void testSoftRegisterUser_ShouldSuccessfullyRegister() throws Exception {
        String phone = "+69420961111";
        String username = "soft_register";

        assertThat(userRepository.findByPhone(phone)).isEmpty();

        userService.softRegisterUser(phone, username);

        assertThat(userRepository.findByPhone(phone)).isPresent();
    }

    @Test
    void testSoftRegisterUser_ExistingUserWithCorrectUsername_ShouldSuccessfullyRegister() throws Exception {
        String phone = "+1234567890";
        String username = "admin";

        assertThat(userRepository.findByPhone(phone)).isPresent();

        userService.softRegisterUser(phone, username);

        assertThat(userRepository.findByPhone(phone)).isPresent();
    }

    @Test
    void testSoftRegisterUser_ExistingUserWithIncorrectUsername_ExceptionThrown() {
        String phone = "+1234567890";
        String username = "adminWrong";

        assertThat(userRepository.findByPhone(phone)).isPresent();

        assertThrows(Exception.class, () -> userService.softRegisterUser(phone, username));
    }

    @Test
    void testVerifyOTPAndAddRole_WithValidOTP_ShouldSuccessfullyVerify() throws Exception {
        String phone = "+1234567890";
        String otp = "123456";
        Role.RoleNames roleName = Role.RoleNames.ROLE_ADMIN;

        // Set otp to KV Service
        kvService.set("otp_" + phone, otp, 900L);

        assertThat(userService.verifyOTPAndAddRole(phone, otp, roleName)).isNotNull();
    }

    @Test
    void testVerifyOTPAndAddRole_WithInvalidOTP_ShouldThrowException() {
        String phone = "+9906345322";
        String otp = "123456";
        String invalidOtp = "654321";

        Role.RoleNames roleName = Role.RoleNames.ROLE_ADMIN;

        // Set otp to KV Service
        kvService.set("otp_" + phone, otp, 900L);

        assertThrows(Exception.class, () -> userService.verifyOTPAndAddRole(phone, invalidOtp, roleName));
    }

    @Test
    void testVerifyOTPAndAddRole_WithExpiredOTP_ShouldThrowException() {
        String phone = "7688453415";
        String otp = "123456";

        Role.RoleNames roleName = Role.RoleNames.ROLE_ADMIN;

        // OTP not saved to KV since expired OTPs are deleted
        assertThrows(Exception.class, () -> userService.verifyOTPAndAddRole(phone, otp, roleName));
    }

    @Test
    void testUpdateUser_ShouldSuccessfullyUpdate() {
        String username = "raph";
        User user = userRepository.findByUsername(username).orElseThrow();

        String newUsername = "raph2";
        user.setUsername(newUsername);

        userService.saveUser(user);

        assertThat(userRepository.findByUsername(newUsername)).isPresent();
    }

    @Test
    void testUpdateUser_WhenUsernameIsAlreadyUsed_ShouldFail() {
        String username = "raph";
        User user = userRepository.findByUsername(username).orElseThrow();

        String newUsername = "matt";
        user.setUsername(newUsername);

        assertThrows(Exception.class, () -> userService.saveUser(user));
    }

    @Test
    void testGenerateOTPAndSaveToKV_ShouldBeSuccessful() {
        String phone = "1266784472";
        String otp = userService.generateOTPAndSaveToKV(phone, 900L);
        assertThat(otp).isNotNull();
        assertThat(kvService.get("otp_" + phone)).isEqualTo(otp);
    }

    // TODO: Upload Image Unit Test

    @Test
    void testLoadUserByUsername_ShouldReturnUserDetails() {
        String username = "admin";
        assertThat(userService.loadUserByUsername(username)).isNotNull();
    }

    @Test
    void testLoadUserByUsername_WhenUsernameDoesNotExist_ShouldThrowUsernameNotFoundException() {
        String username = "adminWrong";
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
    }
}
