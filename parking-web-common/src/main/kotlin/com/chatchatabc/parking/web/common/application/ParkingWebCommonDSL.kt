package com.chatchatabc.parking.web.common.application

import com.chatchatabc.parking.domain.SpringContextUtils
import com.chatchatabc.parking.web.common.application.enums.NatsPayloadTypes
import com.chatchatabc.parking.web.common.application.enums.ResponseNames
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

fun <T> Optional<T>.toResponse(): ResponseEntity<ApiResponse<T>> {
    if (this.isPresent) {
        return ResponseEntity.ok(ApiResponse(this.get(), listOf()))
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_NOT_FOUND.name, null))))
}

fun <T> T.toResponse(): ResponseEntity<ApiResponse<T>> {
    return ResponseEntity.ok(ApiResponse(this, emptyList()))
}

fun <T> T.toNullResponse(): ResponseEntity<ApiResponse<T>> {
    return ResponseEntity.ok(ApiResponse(null, emptyList()))
}

fun <T> Optional<T>.toNullableResponse(): ResponseEntity<ApiResponse<T>> {
    return ResponseEntity.ok(ApiResponse(this.orElse(null), listOf()))
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

val exceptionStatusMapping = mapOf<Class<out Throwable>, HttpStatus>(
    // TODO: Add other exceptions and their corresponding status codes
    jakarta.validation.ConstraintViolationException::class.java to HttpStatus.BAD_REQUEST
)

fun extractDetailMessage(fullMessage: String?): String {
    // This regex pattern looks for the substring that starts with "Detail: " and ends with a closing square bracket
    val pattern = "Detail: (.*?)]".toPattern()
    val matcher = pattern.matcher(fullMessage ?: "")
    return if (matcher.find()) {
        // return the extracted detail message
        matcher.group(1) ?: "Unknown error"
    } else {
        // Return the full message if the detail part cannot be extracted
        fullMessage ?: "Unknown error"
    }
}


fun <T : Throwable> T.toErrorResponse(): ResponseEntity<ApiResponse<Nothing>> {
    this.printStackTrace()
    val errorList = mutableListOf<ErrorElement>()
    val status = exceptionStatusMapping[this::class.java] ?: HttpStatus.BAD_REQUEST

    when (this) {
        is jakarta.validation.ConstraintViolationException -> {
            this.constraintViolations.forEach {
                errorList.add(ErrorElement(ResponseNames.ERROR_CREATE.name, it.messageTemplate))
            }
        }

        is org.springframework.dao.DataIntegrityViolationException -> {
            val detailMessage = extractDetailMessage(this.message)
            errorList.add(ErrorElement(ResponseNames.ERROR_CREATE.name, detailMessage))
        }
        // TODO: Handle other specific exceptions
        else -> {
            errorList.add(ErrorElement(ResponseNames.ERROR.name, this.message))
        }
    }

    return ResponseEntity(ApiResponse(null, errorList), status)
}

data class NatsMessage<T>(
    val type: NatsPayloadTypes,
    val payload: T?
)

fun <T> NatsMessage<T>.toJson(): String {
    return SpringContextUtils.getObjectMapper().writeValueAsString(this)
}
