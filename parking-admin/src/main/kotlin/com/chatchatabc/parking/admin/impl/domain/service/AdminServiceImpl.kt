package com.chatchatabc.parking.admin.impl.domain.service

import com.chatchatabc.parking.admin.domain.service.AdminService
import com.chatchatabc.parking.domain.enums.RoleNames
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.RoleRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminServiceImpl(
        @Value("\${server.admin.email}")
        private var adminEmail: String,
        @Value("\${server.admin.username}")
        private val adminUsername: String,
        @Value("\${server.admin.password}")
        private val adminPassword: String,
        @Value("\${server.admin.firstname}")
        private val adminFirstname: String,
        @Value("\${server.admin.lastname}")
        private val adminLastname: String,

        private val userRepository: UserRepository,
        private val roleRepository: RoleRepository,
        private val passwordEncoder: PasswordEncoder
) : AdminService {
    /**
     * Initialize admin user
     */
    @Transactional
    override fun initAdmin() {
        if (!adminUserExists()) {
            val adminRole = roleRepository.findByName(RoleNames.ROLE_ADMIN.name)
            if (adminRole.isEmpty) {
                throw RuntimeException("Admin role not found")
            }
            val adminUser = User()
            adminUser.email = adminEmail
            adminUser.username = adminUsername
            adminUser.password = passwordEncoder.encode(adminPassword)
            adminUser.firstName = adminFirstname
            adminUser.lastName = adminLastname
            adminUser.roles = mutableSetOf(adminRole.get())
            userRepository.save(adminUser)
        }
    }

    /**
     * Check if admin user exists
     */
    override fun adminUserExists(): Boolean {
        val adminRoleName = RoleNames.ROLE_ADMIN
        val adminRoleCount = userRepository.countUsersByRole(adminRoleName.name)
        return adminRoleCount > 0
    }
}