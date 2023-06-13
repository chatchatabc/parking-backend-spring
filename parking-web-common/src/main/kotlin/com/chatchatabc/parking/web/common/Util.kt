package com.chatchatabc.parking.web.common

import com.chatchatabc.parking.domain.enums.ResponseNames
import java.util.*

data class ApiResponse<T>(
    val data: T? = null,
    val errors: List<ErrorElement>? = null
)

data class ErrorElement(
    val title: String?,
    val message: String?
)

fun <T> Optional<T>.toResponse(): ApiResponse<T> {
    if (this.isPresent) {
        return ApiResponse(this.get(), null)
    }
    return ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_NOT_FOUND.name, null)))
}

fun <T> T.toResponse(): ApiResponse<T> {
    return ApiResponse(this, emptyList())
}

fun <T : Throwable> T.toErrorResponse(): ApiResponse<Nothing> {
    // TODO: Add logic for every exception
    val errorList = mutableListOf<ErrorElement>()
    errorList.add(ErrorElement(ResponseNames.ERROR.name, this.message))
    return ApiResponse(null, errorList)
}