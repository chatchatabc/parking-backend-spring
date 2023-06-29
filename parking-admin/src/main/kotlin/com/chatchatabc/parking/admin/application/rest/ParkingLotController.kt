package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.ParkingLotMapper
import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.model.file.CloudFile
import com.chatchatabc.parking.domain.parkingLot
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import com.chatchatabc.parking.domain.repository.file.ParkingLotImageRepository
import com.chatchatabc.parking.domain.service.ParkingLotService
import com.chatchatabc.parking.domain.service.file.ParkingLotImageService
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.infra.service.FileStorageService
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletResponse
import org.mapstruct.factory.Mappers
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/parking-lot")
class ParkingLotController(
    private val parkingLotService: ParkingLotService,
    private val parkingLotImageService: ParkingLotImageService,
    private val parkingLotImageRepository: ParkingLotImageRepository,
    private val fileStorageService: FileStorageService,
    private val invoiceRepository: InvoiceRepository
) {
    private val parkingLotMapper = Mappers.getMapper(ParkingLotMapper::class.java)

    /**
     * Admin create Parking Lot
     */
    @Operation(
        summary = "Create Parking Lot",
        description = "Create Parking Lot"
    )
    @PostMapping("/{userUuid}")
    fun createParkingLot(
        @PathVariable userUuid: String,
        @RequestBody req: ParkingLotMapper.ParkingLotMapDTO
    ) = runCatching {
        val createdParkingLot = ParkingLot()
        createdParkingLot.owner = userUuid.user.id
        createdParkingLot.availableSlots = req.capacity
        parkingLotMapper.mapRequestToParkingLot(req, createdParkingLot)
        parkingLotService.saveParkingLot(createdParkingLot).toResponse()
    }.getOrElse { it.toErrorResponse() }


    /**
     * Admin update Parking Lot
     */
    @Operation(
        summary = "Update Parking Lot",
        description = "Update Parking Lot"
    )
    @PutMapping("/{parkingLotUuid}")
    fun updateParkingLot(
        @PathVariable parkingLotUuid: String,
        @RequestBody req: ParkingLotMapper.ParkingLotMapDTO,
        principal: Principal
    ) = runCatching {
        // Map request to parking lot
        val updatedParkingLot = parkingLotUuid.parkingLot
        parkingLotMapper.mapRequestToParkingLot(req, updatedParkingLot)

        // Update available slots if there are active invoices and if capacity is updated
        val activeInvoices =
            invoiceRepository.countActiveInvoicesByParkingLotUuid(updatedParkingLot.parkingLotUuid)
        updatedParkingLot.availableSlots = req.capacity - activeInvoices.toInt()

        // Override available slots if available slots is not null
        updatedParkingLot.availableSlots = req.availableSlots

        // Update images
        if (req.images.isNullOrEmpty().not()) {
            parkingLotImageService.updateOrderOfImages(req.images)
        }
        // Save
        parkingLotService.saveParkingLot(updatedParkingLot).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Verify Parking Lot
     */
    @Operation(
        summary = "Verify Parking Lot",
        description = "Verify Parking Lot"
    )
    @PutMapping("/verify/{parkingLotUuid}")
    fun verifyParkingLot(
        @PathVariable parkingLotUuid: String,
        principal: Principal
    ) = runCatching {
        parkingLotService.verifyParkingLot(principal.name, parkingLotUuid).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Delete Image
     */
    @Operation(
        summary = "Delete an Image of a Parking Lot",
        description = "Delete an Image of a Parking Lot"
    )
    @PostMapping("/delete-image/{imageId}")
    fun deleteImage(
        @PathVariable imageId: String
    ) = runCatching {
        parkingLotImageService.deleteImage(imageId).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Restore Image
     */
    @Operation(
        summary = "Restore Image of a Parking Lot",
        description = "Restore Image of a Parking Lot"
    )
    @PostMapping("/restore-image/{imageId}")
    fun restoreImage(
        @PathVariable imageId: String
    ) = runCatching {
        parkingLotImageService.restoreImage(imageId).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Get parking lot avatar by image uuid
     */
    @Operation(
        summary = "Get Image of a Parking Lot",
        description = "Get Image of a Parking Lot"
    )
    @GetMapping("/image/{imageUuid}")
    fun getParkingLotImage(
        @PathVariable imageUuid: String,
        response: HttpServletResponse
    ) = runCatching {
        val image = parkingLotImageRepository.findByIdAndStatus(imageUuid, CloudFile.ACTIVE).get()
        if (image.cloudFile == null) {
            throw Exception("Image not found")
        }
        response.contentType = image.cloudFile.mimeType
        // Add 1 day cache
        response.setHeader("Cache-Control", "max-age=86400")
        val inputStream = fileStorageService.downloadFile(image.cloudFile.key)
        inputStream.copyTo(response.outputStream)
        response.flushBuffer()
    }.getOrElse {
        response.sendError(HttpServletResponse.SC_NOT_FOUND)
    }

    /**
     * Get featured parking lot image if exists
     */
    @Operation(
        summary = "Get Featured Image of a Parking Lot",
        description = "Get Featured Image of a Parking Lot"
    )
    @GetMapping("/featured-image/{parkingLotUuid}")
    fun getFeaturedParkingLotImage(
        @PathVariable parkingLotUuid: String,
        response: HttpServletResponse
    ) = runCatching {
        val pr = PageRequest.of(0, 1)
        val parkingLot = parkingLotUuid.parkingLot
        val image = parkingLotImageRepository.findAllByParkingLotAndStatus(
            parkingLot.id,
            CloudFile.ACTIVE,
            pr
        ).content.firstOrNull() ?: throw Exception("Image not found")

        if (image.cloudFile == null) {
            throw Exception("Image not found")
        }

        response.contentType = image.cloudFile.mimeType
        // Add 1 day cache
        response.setHeader("Cache-Control", "max-age=86400")
        val inputStream = fileStorageService.downloadFile(image.cloudFile.key)
        inputStream.copyTo(response.outputStream)
        response.flushBuffer()
    }.getOrElse {
        response.sendError(HttpServletResponse.SC_NOT_FOUND)
    }
}