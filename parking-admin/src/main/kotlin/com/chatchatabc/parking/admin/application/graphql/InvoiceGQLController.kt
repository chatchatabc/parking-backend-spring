package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.model.Invoice
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class InvoiceGQLController(
    private val invoiceRepository: InvoiceRepository
) {
    /**
      * Get invoices
     */
    @QueryMapping
    fun getInvoices(
        @Argument page: Int,
        @Argument size: Int
    ): PagedResponse<Invoice> {
        val pr = PageRequest.of(page, size)
        val invoices = invoiceRepository.findAll(pr)
        return PagedResponse(
            invoices.content,
            PageInfo(
                invoices.size,
                invoices.totalElements,
                invoices.isFirst,
                invoices.isLast,
                invoices.isEmpty
            )
        )
    }

    /**
     * Get invoice by uuid
     */
    @QueryMapping
    fun getInvoiceByUuid(
        @Argument uuid: String
    ): Optional<Invoice> {
        return invoiceRepository.findById(uuid)
    }
}