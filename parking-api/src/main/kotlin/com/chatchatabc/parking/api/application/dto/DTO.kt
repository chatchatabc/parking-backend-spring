package com.chatchatabc.parking.api.application.dto

import com.chatchatabc.parking.web.common.application.enums.NatsPayloadTypes

data class ApiResponse<T>(
    val data: T? = null,
    val errors: List<ErrorElement>? = null
)

data class ErrorElement(
    val title: String?,
    val message: String?
)

data class NatsMessage<T>(
    val type: NatsPayloadTypes,
    val payload: T?
)

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