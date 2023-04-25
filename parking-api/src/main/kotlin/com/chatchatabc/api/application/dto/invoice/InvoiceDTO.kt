package com.chatchatabc.api.application.dto.invoice

import com.chatchatabc.api.application.dto.ErrorContent
import com.chatchatabc.parking.domain.model.Invoice

data class InvoiceResponse(
    val invoice: Invoice?,
    val error: ErrorContent?
)
