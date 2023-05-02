package com.chatchatabc.parking.admin.application.dto

data class PageInfo(
    val size: Int,
    val totalElements: Long,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean
)
