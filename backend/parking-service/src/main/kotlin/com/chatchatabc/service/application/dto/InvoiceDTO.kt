package com.chatchatabc.service.application.dto

import com.chatchatabc.api.application.dto.ErrorContent
import com.chatchatabc.service.domain.model.Invoice

data class InvoiceResponse(
    val invoice: Invoice?,
    val error: ErrorContent?
)