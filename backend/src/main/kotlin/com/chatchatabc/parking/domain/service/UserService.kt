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
     * Check if user is fully registered
     */
    fun checkIfUserIsFullyRegistered(phone: String)

    /**
     * Check if user is fully registered w/ username
     */
    fun checkIfUserIsFullyRegistered(phone: String, username: String)

    /**
     * Create OTP and send via SMS
     */
    fun createOTPAndSendSMS(phone: String)

    /**
     * Verify if OTP and Phone is correct
     */
    fun parkingVerifyOTP(phone: String, otp: String): User

    /**
     * Update user
     */
    fun updateUser(userId: String, username: String?, email: String?, firstName: String?, lastName: String?): User
}