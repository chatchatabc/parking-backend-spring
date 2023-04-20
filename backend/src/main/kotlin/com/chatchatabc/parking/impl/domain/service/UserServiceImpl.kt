package com.chatchatabc.parking.impl.domain.service

import com.chatchatabc.parking.domain.model.RoleNames
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.RoleRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.service.UserService
import com.chatchatabc.parking.infra.service.JedisService
import com.chatchatabc.parking.infra.service.UtilService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val roleRepository: RoleRepository,
    private val jedisService: JedisService,
    private val utilService: UtilService
) : UserService {
    /**
     * Register a new user
     */
    override fun register(user: User, roleName: RoleNames): User {
        // Get Role
        val role = roleRepository.findByName(roleName.name)
        // Encode Password
        if (user.password != null) {
            user.setPassword(passwordEncoder.encode(user.password))
        }
        // Set Role
        user.roles.add(role.get())
        return userRepository.save(user)
    }

    /**
     * Check if user is fully registered
     */
    override fun checkIfUserIsFullyRegistered(phone: String) {
        val queriedUser = userRepository.findByPhone(phone)
        if (queriedUser.isEmpty) {
            val createdUser = User()
            createdUser.phone = phone
            userRepository.save(createdUser)
        }
    }

    /**
     * Check if user is fully registered w/ username
     */
    override fun checkIfUserIsFullyRegistered(phone: String, username: String) {
        val queriedUser = userRepository.findByPhone(phone)
        if (queriedUser.isEmpty) {
            val createdUser = User()
            createdUser.phone = phone
            createdUser.setUsername(username)
            userRepository.save(createdUser)
        } else {
            if (queriedUser.get().username != username) {
                throw Exception("Username is incorrect")
            }
        }
    }

    /**
     * Create OTP and send via SMS
     */
    override fun createOTPAndSendSMS(phone: String) {
        // Create OTP
        val otp = utilService.generateOTP()
        jedisService.set("otp_$phone", otp, 900)
        println("Phone: $phone, OTP: $otp")
        // TODO: Send SMS
    }

    /**
     * Verify if OTP and Phone is correct
     */
    override fun verifyOTP(phone: String, otp: String, roleName: RoleNames): User {
        val queriedUser = userRepository.findByPhone(phone)
        if (queriedUser.isEmpty) {
            throw Exception("User not found")
        }
        val savedOTP = jedisService.get("otp_$phone")
        // TODO: REMOVE IN THE FUTURE
        if (savedOTP != "000000") {
            if (savedOTP != otp) {
                throw Exception("OTP is incorrect")
            }
        }
        // Delete OTP on Redis if done
        jedisService.get("otp_$phone")
        // Update phone verified date only if null, no need to reset again
        if (queriedUser.get().phoneVerifiedAt == null) {
            queriedUser.get().phoneVerifiedAt = Date()
        }
        // Add Parking Manager Role to User
        val role = roleRepository.findByName(roleName.name)
        queriedUser.get().roles.add(role.get())
        userRepository.save(queriedUser.get())
        return queriedUser.get()
    }

    /**
     * Update user
     */
    override fun updateUser(
        userId: String,
        username: String?,
        email: String?,
        firstName: String?,
        lastName: String?
    ): User {
        val queriedUser = userRepository.findById(userId)
        if (queriedUser.isEmpty) {
            throw Exception("User not found")
        }

        // Apply changes
        if (username != null) {
            queriedUser.get().setUsername(username)
        }
        if (email != null) {
            queriedUser.get().email = email
        }
        if (firstName != null) {
            queriedUser.get().firstName = firstName
        }
        if (lastName != null) {
            queriedUser.get().lastName = lastName
        }
        return userRepository.save(queriedUser.get())
    }

    /**
     * Load user by username for Login
     */
    override fun loadUserByUsername(username: String): UserDetails {
        val user: Optional<User> = userRepository.findByUsername(username)
        if (user.isEmpty) {
            throw Exception("User not found")
        }
        return org.springframework.security.core.userdetails.User(
            user.get().username,
            user.get().password,
            user.get().isEnabled,
            user.get().isAccountNonExpired,
            user.get().isCredentialsNonExpired,
            user.get().isAccountNonLocked,
            user.get().authorities
        )
    }
}