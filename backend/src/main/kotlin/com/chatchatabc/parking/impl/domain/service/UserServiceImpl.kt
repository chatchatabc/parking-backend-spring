package com.chatchatabc.parking.impl.domain.service

import com.chatchatabc.parking.domain.model.RoleNames
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.RoleRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.service.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl (
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val roleRepository: RoleRepository
) : UserService {
    /**
     * Register a new user
     */
    override fun register(user: User, roleName: RoleNames): User {
        // Get Role
        val role = roleRepository.findByName(roleName.name)
        // Encode Password
        user.password = passwordEncoder.encode(user.password)
        // Set Role
        user.roles.add(role.get())
        return userRepository.save(user)
    }

    /**
     * Load user by username for Login
     */
    override fun loadUserByUsername(username: String?): UserDetails {
        TODO("Not yet implemented")
    }
}