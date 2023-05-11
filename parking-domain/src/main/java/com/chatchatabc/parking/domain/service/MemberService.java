package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.enums.RoleNames;
import com.chatchatabc.parking.domain.model.Member;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.InputStream;

public interface MemberService extends UserDetailsService {
    /**
     * Soft register a new member if not exists
     *
     * @param phone    the phone number
     * @param username the username
     */
    void softRegisterMember(String phone, String username) throws Exception;

    /**
     * Verify if OTP and phone number is valid
     *
     * @param phone    the phone number
     * @param roleName the role name
     * @return the member
     */
    Member verifyOTPAndAddRole(String phone, String otp, RoleNames roleName) throws Exception;

    /**
     * Update member
     *
     * @param phone     the phone
     * @param memberId  the member id
     * @param username  the username
     * @param email     the email
     * @param firstName the first name
     * @param lastName  the last name
     * @return the member
     */
    Member updateMember(String memberId, String phone, String username, String email, String firstName, String lastName) throws Exception;

    /**
     * Generate OTP and save to KV
     *
     * @return OTP
     */
    String generateOTPAndSaveToKV(String phone, Long duration);

    /**
     * Upload a file to the storage service.
     *
     * @param uploadedBy  the member who uploaded the file
     * @param namespace   the namespace of the file
     * @param inputStream the file to upload
     * @param filename    the filename
     * @param filesize    the filesize
     * @param mimetype    the mimetype
     * @return the updated member
     * @throws Exception if an error occurs
     */
    Member uploadImage(Member uploadedBy, String namespace, InputStream inputStream, String filename, Long filesize, String mimetype) throws Exception;
}
