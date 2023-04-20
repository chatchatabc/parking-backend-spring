package com.chatchatabc.service.domain.service

import com.chatchatabc.service.domain.model.Invoice
import org.springframework.stereotype.Service

@Service
interface InvoiceService {

    /**
     * Create an invoice
     */
    fun createInvoice(parkingLotId: String, vehicleId: String): Invoice

    /**
     * End an invoice
     */
    fun endInvoice(parkingLotId: String, invoiceId: String): Invoice

    /**
     * Pay an invoice
     */
    fun payInvoice(parkingLotId: String, invoiceId: String): Invoice
}