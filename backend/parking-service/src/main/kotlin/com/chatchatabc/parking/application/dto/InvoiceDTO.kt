package com.chatchatabc.parking.application.dto

import com.chatchatabc.parking.domain.model.Invoice

data class InvoiceResponse(
    val invoice: Invoice?,
    val error: ErrorContent?
)