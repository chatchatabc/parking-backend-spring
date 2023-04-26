package com.chatchatabc.admin.domain.service

import org.springframework.stereotype.Service

@Service
interface AdminService {

    /**
     * Initialize admin user
     */
    fun initAdmin()

    /**
     * Check if admin user exists
     */
    fun adminUserExists(): Boolean
}