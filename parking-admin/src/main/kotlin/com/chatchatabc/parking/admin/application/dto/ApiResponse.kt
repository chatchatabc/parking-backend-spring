package com.chatchatabc.parking.admin.application.dto

data class ApiResponse<T>(
        val data: T?,
        val code: Int = 0,
        val message: String?,
        val error: Boolean = false
)