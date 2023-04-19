package com.chatchatabc.parking.infra.service

import org.springframework.stereotype.Service

@Service
interface UtilService {
    /**
     * Generate OTP
     */
    fun generateOTP(): String
}