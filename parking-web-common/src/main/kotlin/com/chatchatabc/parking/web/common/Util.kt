package com.chatchatabc.parking.web.common

import com.chatchatabc.parking.domain.enums.ResponseNames
import org.springframework.data.domain.Page
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

data class PageInfo(
    val size: Int,
    val totalElements: Long,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean
)

data class PagedResponse<T>(
    val content: List<T>,
    val pageInfo: PageInfo
)

fun <T> Page<T>.toPagedResponse(): PagedResponse<T> {
    return PagedResponse(this.content, PageInfo(this.size, this.totalElements, this.isFirst, this.isLast, this.isEmpty))
}

fun <T : Throwable> T.toErrorResponse(): ApiResponse<Nothing> {
    // TODO: Add logic for every exception
    // TODO: INVOICE_VEHICLE_NOT_PARKED_TODAY
    // TODO: ERROR_CREATE
    val errorList = mutableListOf<ErrorElement>()
    errorList.add(ErrorElement(ResponseNames.ERROR.name, this.message))
    return ApiResponse(null, errorList)
}