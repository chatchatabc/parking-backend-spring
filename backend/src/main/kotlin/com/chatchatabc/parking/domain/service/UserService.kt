package com.chatchatabc.parking.domain.service

import com.chatchatabc.parking.domain.model.RoleNames
import com.chatchatabc.parking.domain.model.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
interface UserService : UserDetailsService {
    /**
     * Register a new user
     */
    fun register(user: User, roleName: RoleNames): User

    /**
     * Soft register a user if not exist
     */
    fun softRegisterUser(phone: String, username: String?)

    /**
     * Create OTP and send via SMS
     */
    fun createOTPAndSendSMS(phone: String)

    /**
     * Verify if OTP and Phone is correct
     */
    fun verifyOTP(phone: String, otp: String, roleName: RoleNames): User

    /**
     * Update user
     */
    fun updateUser(userId: String, username: String?, email: String?, firstName: String?, lastName: String?): User
}