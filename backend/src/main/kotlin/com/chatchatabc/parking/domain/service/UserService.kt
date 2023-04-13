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
}