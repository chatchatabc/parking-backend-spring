package com.chatchatabc.parking.api.application.dto.member

data class MemberPhoneLoginRequest(
    val phone: String,
    val username: String?
)

data class MemberProfileUpdateRequest(
    val username: String?,
    val phone: String?,
    val email: String?,
    val firstName: String?,
    val lastName: String?
)

data class MemberVerifyOTPRequest(
    val phone: String,
    val otp: String
)

data class MemberNotificationResponse(
    val notificationId: String
)