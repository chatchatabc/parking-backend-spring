package com.chatchatabc.parking.admin.application.dto

data class PagedResponse<T>(
    val content: List<T>,
    val pageInfo: PageInfo
)