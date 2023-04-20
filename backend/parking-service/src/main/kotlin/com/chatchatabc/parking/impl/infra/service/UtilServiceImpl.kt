package com.chatchatabc.parking.impl.infra.service

import com.chatchatabc.parking.infra.service.UtilService
import org.springframework.stereotype.Service

@Service
class UtilServiceImpl : UtilService {
    /**
     * Generate OTP
     */
    override fun generateOTP(): String {
        return (0..999999).random().toString().padStart(6, '0')
    }
}