package com.chatchatabc.parking.web.common.application

import com.chatchatabc.parking.domain.SpringContextUtils
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.web.common.application.enums.NatsPayloadTypes
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
        return ApiResponse(this.get(), listOf())
    }
    return ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_NOT_FOUND.name, null)))
}

fun <T> T.toResponse(): ApiResponse<T> {
    return ApiResponse(this, emptyList())
}

fun <T> Optional<T>.toNullableResponse(): ApiResponse<T> {
    return ApiResponse(this.orElse(null), listOf())
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

fun <T : Throwable> T.toErrorResponse(): ResponseEntity<ApiResponse<Nothing>> {
    this.printStackTrace()
    // TODO: Add logic for every exception
    // TODO: INVOICE_VEHICLE_NOT_PARKED_TODAY
    // TODO: ERROR_CREATE
    val errorList = mutableListOf<ErrorElement>()
    errorList.add(ErrorElement(ResponseNames.ERROR.name, this.message))
    // TODO: Add logic to automatically determine status code
    val status = HttpStatus.BAD_REQUEST
    return ResponseEntity(ApiResponse(null, errorList), status)
}

data class NatsMessage<T>(
    val type: NatsPayloadTypes,
    val payload: T?
)

fun <T> NatsMessage<T>.toJson(): String {
    return SpringContextUtils.getObjectMapper().writeValueAsString(this)
}
