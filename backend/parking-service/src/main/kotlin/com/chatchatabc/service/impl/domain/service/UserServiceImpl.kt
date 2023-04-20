package com.chatchatabc.service.impl.domain.service

import com.chatchatabc.api.application.dto.user.UserDTO
import com.chatchatabc.api.domain.enums.RoleNames
import com.chatchatabc.api.domain.service.UserService
import com.chatchatabc.service.domain.model.User
import com.chatchatabc.service.domain.repository.RoleRepository
import com.chatchatabc.service.domain.repository.UserRepository
import com.chatchatabc.service.infra.service.JedisService
import com.chatchatabc.service.infra.service.UtilService
import org.modelmapper.ModelMapper
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val jedisService: JedisService,
    private val utilService: UtilService
) : UserService, UserDetailsService {
    private val modelMapper = ModelMapper()

    /**
     * Register a new user
     */
    override fun registerUser(user: UserDTO, roleName: RoleNames): UserDTO {
        // Get Role
        val role = roleRepository.findByName(roleName.name)
        // Create User
        val createdUser = modelMapper.map(user, User::class.java)
        // Set Role
        createdUser.roles.add(role.get())
        // Map created user back to DTO and return
        return modelMapper.map(userRepository.save(createdUser), UserDTO::class.java)
    }

    /**
     * Check if user is fully registered w/ username
     */
    override fun softRegisterUser(phone: String, username: String?) {
        val queriedUser = userRepository.findByPhone(phone)
        if (queriedUser.isEmpty) {
            val createdUser = User().apply {
                this.phone = phone
                if (username != null) {
                    this.setUsername(username)
                }
            }
            userRepository.save(createdUser)
        } else {
            // Check if username is correct for existing user
            if (!username.isNullOrEmpty()) {
                if (queriedUser.get().username != username) {
                    throw Exception("Username is incorrect")
                }
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
    override fun verifyOTP(phone: String, otp: String, roleName: RoleNames): UserDTO {
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
        return modelMapper.map(queriedUser.get(), UserDTO::class.java)
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
    ): UserDTO {
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
        return modelMapper.map(userRepository.save(queriedUser.get()), UserDTO::class.java)
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