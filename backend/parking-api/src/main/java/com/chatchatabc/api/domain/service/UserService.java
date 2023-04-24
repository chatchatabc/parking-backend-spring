package com.chatchatabc.api.domain.service;

import com.chatchatabc.api.application.dto.user.UserDTO;
import com.chatchatabc.api.domain.enums.RoleNames;

public interface UserService {

    /**
     * Register a new user
     *
     * @param user     the user dto
     * @param roleName the role name
     * @return the user dto
     */
    UserDTO registerUser(UserDTO user, RoleNames roleName);

    /**
     * Soft register a new user if not exists
     *
     * @param phone    the phone number
     * @param username the username
     */
    void softRegisterUser(String phone, String username);

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
     * @return the user dto
     */
    UserDTO verifyOTP(String phone, String otp, RoleNames roleName);

    /**
     * Get user by id
     *
     * @param userId the user id
     * @return the user dto
     */
    UserDTO getUser(String userId);

    /**
     * Update user
     *
     * @param userId    the user id
     * @param username  the username
     * @param email     the email
     * @param firstName the first name
     * @param lastName  the last name
     * @return the user dto
     */
    UserDTO updateUser(String userId, String username, String email, String firstName, String lastName);
}
