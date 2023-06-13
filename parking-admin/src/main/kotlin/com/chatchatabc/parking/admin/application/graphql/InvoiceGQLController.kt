package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.domain.invoice
import com.chatchatabc.parking.domain.parkingLot
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.specification.InvoiceSpecification
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.domain.vehicle
import com.chatchatabc.parking.web.common.application.toPagedResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class InvoiceGQLController(
    private val invoiceRepository: InvoiceRepository,
    private val vehicleRepository: VehicleRepository,
) {
    /**
     * Get invoices
     */
    @QueryMapping
    fun getInvoices(
        @Argument page: Int,
        @Argument size: Int,
        @Argument keyword: String?,
        @Argument sortField: String? = null,
        @Argument sortBy: Int? = null,
    ) = run {
        val pr = PageRequest.of(page, size)
        var spec = InvoiceSpecification.withKeyword(keyword ?: "")

        // Sort
        if (sortField != null && sortBy != null) {
            spec = spec.and(InvoiceSpecification.sortBy(sortField, sortBy))
        }
        invoiceRepository.findAll(spec, pr).toPagedResponse()
    }

    /**
     * Get invoice by identifier
     */
    @QueryMapping
    fun getInvoice(@Argument id: String) = run { id.invoice }

    /**
     * Get invoices by vehicle uuid
     */
    @QueryMapping
    fun getInvoicesByVehicle(
        @Argument page: Int,
        @Argument size: Int,
        @Argument id: String
    ) = run {
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        val pr = PageRequest.of(page, size, sort)
        invoiceRepository.findAllByVehicle(id.vehicle.vehicleUuid, pr).toPagedResponse()
    }

    /**
     * Get invoices by parking lot uuid
     */
    @QueryMapping
    fun getInvoicesByParkingLot(
        @Argument page: Int,
        @Argument size: Int,
        @Argument id: String
    ) = run {
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        val pr = PageRequest.of(page, size, sort)
        invoiceRepository.findAllByParkingLot(id.parkingLot.parkingLotUuid, pr).toPagedResponse()
    }

    /**
     * Get invoices by user uuid
     */
    @QueryMapping
    fun getInvoicesByUser(
        @Argument page: Int,
        @Argument size: Int,
        @Argument id: String
    ) = run {
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        val pr = PageRequest.of(page, size, sort)
        val vehicles = vehicleRepository.findVehicleIdsByOwner(id.user.id)
        invoiceRepository.findAllByVehicles(vehicles, pr).toPagedResponse()
    }
}