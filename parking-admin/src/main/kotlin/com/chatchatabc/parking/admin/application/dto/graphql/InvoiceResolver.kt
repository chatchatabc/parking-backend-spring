package com.chatchatabc.parking.admin.application.dto.graphql

import com.chatchatabc.parking.domain.model.Invoice
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class InvoiceResolver(
    private val invoiceRepository: InvoiceRepository
) {
    /**
     * Get invoices
     */
    @QueryMapping
    fun getInvoices(
        @Argument page: Int,
        @Argument size: Int
    ): Page<Invoice> {
        val pr = PageRequest.of(page, size)
        return invoiceRepository.findAll(pr)
    }
}