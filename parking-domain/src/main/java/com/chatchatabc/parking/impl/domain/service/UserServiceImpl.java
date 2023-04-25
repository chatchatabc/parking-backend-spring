package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.enums.RoleNames;
import com.chatchatabc.parking.domain.model.Role;
import com.chatchatabc.parking.domain.model.User;
import com.chatchatabc.parking.domain.repository.RoleRepository;
import com.chatchatabc.parking.domain.repository.UserRepository;
import com.chatchatabc.parking.domain.service.UserService;
import com.chatchatabc.parking.infra.service.JedisService;
import com.chatchatabc.parking.infra.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private UtilService utilService;

    /**
     * Soft register a new user if not exists
     *
     * @param phone    the phone number
     * @param username the username
     */
    @Override
    public void softRegisterUser(String phone, String username) throws Exception {
        Optional<User> queriedUser = userRepository.findByPhone(phone);
        if (queriedUser.isEmpty()) {
            User createdUser = new User();
            createdUser.setPhone(phone);
            if (username != null) {
                createdUser.setUsername(username);
            }
            userRepository.save(createdUser);
        } else {
            // Check if username is correct for existing user
            if (username != null) {
                if (!Objects.equals(queriedUser.get().getUsername(), username)) {
                    throw new Exception("Username is incorrect");
                }
            }
        }
    }

    /**
     * Create OTP and send SMS
     *
     * @param phone the phone number
     */
    @Override
    public void createOTPAndSendSMS(String phone) {
        // Create OTP
        String otp = utilService.generateOTP();
        jedisService.set("otp_" + phone, otp, 900L);
        System.out.println("Phone: " + phone + ", OTP: " + otp);
        // TODO: Send SMS using events
    }

    /**
     * Verify if OTP and phone number is valid
     *
     * @param phone    the phone number
     * @param otp      the otp
     * @param roleName the role name
     * @return the user
     */
    @Override
    public User verifyOTP(String phone, String otp, RoleNames roleName) throws Exception {
        Optional<User> queriedUser = userRepository.findByPhone(phone);
        if (queriedUser.isEmpty()) {
            throw new Exception("User not found");
        }
        Optional<Role> role = roleRepository.findByName(roleName.name());
        if (role.isEmpty()) {
            throw new Exception("Role not found");
        }
        // Check if OTP is correct
        String savedOTP = jedisService.get("otp_" + phone);
        // TODO: Remove "000000" override in the future
        if (!Objects.equals(savedOTP, otp) && !Objects.equals(savedOTP, "000000")) {
            throw new Exception("OTP is incorrect");
        }
        // Delete OTP on Redis if done
        jedisService.del("otp_" + phone);
        // Update phone verified date only if null, no need to reset again
        if (queriedUser.get().getPhoneVerifiedAt() == null) {
            // Set Phone Verified At to current local date time
            queriedUser.get().setPhoneVerifiedAt(LocalDateTime.now());
        }
        // Add role to user if not exist
        if (queriedUser.get().getRoles().stream().noneMatch(r -> Objects.equals(r.getId(), role.get().getId()))) {
            queriedUser.get().getRoles().add(role.get());
        }
        return userRepository.save(queriedUser.get());
    }

    /**
     * Update user
     *
     * @param userId    the user id
     * @param username  the username
     * @param email     the email
     * @param firstName the first name
     * @param lastName  the last name
     * @return the user
     */
    @Override
    public User updateUser(String userId, String username, String email, String firstName, String lastName) throws Exception {
        Optional<User> queriedUser = userRepository.findById(userId);
        if (queriedUser.isEmpty()) {
            throw new Exception("User not found");
        }

        // Apply Changes
        if (username != null) {
            queriedUser.get().setUsername(username);
        }
        if (email != null) {
            queriedUser.get().setEmail(email);
        }
        if (firstName != null) {
            queriedUser.get().setFirstName(firstName);
        }
        if (lastName != null) {
            queriedUser.get().setLastName(lastName);
        }
        return userRepository.save(queriedUser.get());
    }
}
