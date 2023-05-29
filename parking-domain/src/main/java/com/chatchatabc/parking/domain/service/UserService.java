package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.enums.RoleNames;
import com.chatchatabc.parking.domain.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.InputStream;

public interface UserService extends UserDetailsService {
    /**
     * Soft register a new user if not exists
     *
     * @param phone    the phone number
     * @param username the username
     */
    void softRegisterUser(String phone, String username) throws Exception;

    /**
     * Verify if OTP and phone number is valid
     *
     * @param phone    the phone number
     * @param roleName the role name
     * @return the user
     */
    User verifyOTPAndAddRole(String phone, String otp, RoleNames roleName) throws Exception;

    /**
     * Update user
     *
     * @param updatedUser the updated user
     */
    void saveUser(User updatedUser) throws Exception;

    /**
     * Generate OTP and save to KV
     *
     * @return OTP
     */
    String generateOTPAndSaveToKV(String phone, Long duration);

    /**
     * Upload a file to the storage service.
     *
     * @param uploadedBy  the user who uploaded the file
     * @param namespace   the namespace of the file
     * @param inputStream the file to upload
     * @param filename    the filename
     * @param filesize    the filesize
     * @param mimetype    the mimetype
     * @return the updated user
     * @throws Exception if an error occurs
     */
    User uploadImage(User uploadedBy, String namespace, InputStream inputStream, String filename, Long filesize, String mimetype) throws Exception;
}
