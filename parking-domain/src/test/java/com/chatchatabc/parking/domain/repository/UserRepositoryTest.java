package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryTest extends TestContainersBaseTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername_UsernameIsFound() {
        String username = "admin";
        Optional<User> user = userRepository.findByUsername(username);
        assertThat(user).isPresent();
        assertThat(user.get().getUsername()).isEqualTo(username);
    }

    @Test
    void testFindByUsername_UsernameNotFound() {
        String username = "NON-EXISTENT";
        Optional<User> user = userRepository.findByUsername(username);
        assertThat(user).isNotPresent();
    }

    @Test
    void testFindByPhone_PhoneIsFound() {
        String phone = "+1234567890";
        Optional<User> user = userRepository.findByPhone(phone);
        assertThat(user).isPresent();
        assertThat(user.get().getPhone()).isEqualTo(phone);
    }

    @Test
    void testFindByPhone_PhoneNotFound() {
        String phone = "+9999999999";
        Optional<User> user = userRepository.findByPhone(phone);
        assertThat(user).isNotPresent();
    }

    @Test
    void testCountUsersByRole_RoleNameExists() {
        String roleName = "ROLE_ADMIN";
        Long count = userRepository.countUsersByRole(roleName);
        assertThat(count).isEqualTo(1L);
    }

    @Test
    void testCountUsersByRole_RoleNameDoesNotExist() {
        String roleName = "ROLE_NONEXISTENT";
        Long count = userRepository.countUsersByRole(roleName);
        assertThat(count).isEqualTo(0L);
    }

    @Test
    void testFindByUserUuid_UuidIsFound() {
        String userUuid = "ec4af6e9-ec57-434d-990d-ae83d9459a31";
        Optional<User> user = userRepository.findByUserUuid(userUuid);
        assertThat(user).isPresent();
        assertThat(user.get().getUserUuid()).isEqualTo(userUuid);
    }

    @Test
    void testFindByUserUuid_UuidNotFound() {
        String userUuid = "00000000-0000-0000-0000-000000000000";
        Optional<User> user = userRepository.findByUserUuid(userUuid);
        assertThat(user).isNotPresent();
    }

    @Test
    void testCountVerified_UsersWithVerification() {
        Long verifiedUsersCount = userRepository.countVerified();
        assertThat(verifiedUsersCount).isGreaterThan(0);
    }

    @Test
    void testFindByEmail_ShouldReturnUser() {
        String email = "admin@gmail.com";
        assertThat(userRepository.findByEmail(email)).isPresent();
    }

    @Test
    void testFindByEmail_ShouldReturnEmpty() {
        String email = "non-existing@gmail.com";
        assertThat(userRepository.findByEmail(email)).isNotPresent();
    }
}