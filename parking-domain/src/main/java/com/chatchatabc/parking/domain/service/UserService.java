package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.enums.RoleNames;
import com.chatchatabc.parking.domain.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    /**
     * Soft register a new user if not exists
     *
     * @param phone    the phone number
     * @param username the username
     */
    void softRegisterUser(String phone, String username) throws Exception;

    /**
     * Create OTP and send SMS
     *
     * @param phone the phone number
     */
    void createOTPAndSendSMS(String phone);

    /**
     * Verify if OTP and phone number is valid
     *
     * @param phone    the phone number
     * @param otp      the otp
     * @param roleName the role name
     * @return the user
     */
    User verifyOTP(String phone, String otp, RoleNames roleName) throws Exception;

    /**
     * Update user
     *
     * @param phone     the phone
     * @param userId    the user id
     * @param username  the username
     * @param email     the email
     * @param firstName the first name
     * @param lastName  the last name
     * @return the user
     */
    User updateUser(String userId, String phone, String username, String email, String firstName, String lastName) throws Exception;
}
