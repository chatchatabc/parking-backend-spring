package com.chatchatabc.api.application.dto

data class ApiResponse(
    val data: Any?,
    val code: Int = 0,
    val message: String?,
    val error: Boolean = false
)