package com.chatchatabc.service.infra.service

import org.springframework.stereotype.Service

@Service
interface UtilService {
    /**
     * Generate OTP
     */
    fun generateOTP(): String
}