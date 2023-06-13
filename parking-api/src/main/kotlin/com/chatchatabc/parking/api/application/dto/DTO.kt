package com.chatchatabc.parking.api.application.dto

data class UserPhoneLoginRequest(
    val phone: String,
    val username: String?
)

data class UserVerifyOTPRequest(
    val phone: String,
    val otp: String
)

data class UserNotificationResponse(
    val notificationId: String?
)

data class ReportCreateRequest(
    val name: String,
    val description: String,
    val plateNumber: String,
    val latitude: Double,
    val longitude: Double,
)