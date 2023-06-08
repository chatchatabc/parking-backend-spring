package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.admin.application.dto.ErrorElement
import com.chatchatabc.parking.admin.application.mapper.ParkingLotMapper
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.model.file.CloudFile
import com.chatchatabc.parking.domain.model.file.ParkingLotImage
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.file.ParkingLotImageRepository
import com.chatchatabc.parking.domain.service.ParkingLotService
import com.chatchatabc.parking.domain.service.file.ParkingLotImageService
import com.chatchatabc.parking.infra.service.FileStorageService
import jakarta.servlet.http.HttpServletResponse
import org.mapstruct.factory.Mappers
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/parking-lot")
class ParkingLotController(
    private val parkingLotService: ParkingLotService,
    private val parkingLotImageService: ParkingLotImageService,
    private val parkingLotImageRepository: ParkingLotImageRepository,
    private val fileStorageService: FileStorageService,
    private val parkingLotRepository: ParkingLotRepository,
    private val userRepository: UserRepository,
    private val invoiceRepository: InvoiceRepository
) {
    private val parkingLotMapper = Mappers.getMapper(ParkingLotMapper::class.java)

    /**
     * Admin create Parking Lot Request
     */
    data class ParkingLotCreateRequest(
        val name: String,
        val latitude: Double,
        val longitude: Double,
        val address: String,
        val description: String,
        val capacity: Int,
        val businessHoursStart: LocalDateTime?,
        val businessHoursEnd: LocalDateTime?,
        val openDaysFlag: Int = 0
    )

    /**
     * Admin create Parking Lot
     */
    @PostMapping("/create/{userUuid}")
    fun createParkingLot(
        @PathVariable userUuid: String,
        @RequestBody req: ParkingLotCreateRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        return try {
            val owner = userRepository.findByUserUuid(userUuid).get()
            val createdParkingLot = ParkingLot()
            createdParkingLot.owner = owner.id
            createdParkingLot.availableSlots = req.capacity
            parkingLotMapper.createParkingLotFromCreateRequest(req, createdParkingLot)
            parkingLotService.saveParkingLot(createdParkingLot)
            ResponseEntity.ok(ApiResponse(null, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null)))
            )
        }
    }


    /**
     * Admin update Parking Lot Request
     */
    data class ParkingLotUpdateRequest(
        val name: String?,
        val latitude: Double?,
        val longitude: Double?,
        val address: String?,
        val description: String?,
        val capacity: Int?,
        val availableSlots: Int?,
        val businessHoursStart: LocalDateTime?,
        val businessHoursEnd: LocalDateTime?,
        val openDaysFlag: Int?,
        val images: List<ParkingLotImage>?
    )

    /**
     * Admin update Parking Lot
     */
    @PutMapping("/update/{parkingLotUuid}")
    fun updateParkingLot(
        @PathVariable parkingLotUuid: String,
        @RequestBody req: ParkingLotUpdateRequest,
        principal: Principal
    ): ResponseEntity<ApiResponse<Nothing>> {
        return try {
            // Map request to parking lot
            val updatedParkingLot = parkingLotRepository.findByParkingLotUuid(parkingLotUuid).get()
            parkingLotMapper.updateParkingLotFromUpdateRequest(req, updatedParkingLot)

            // Update available slots if there are active invoices and if capacity is updated
            if (req.capacity != null) {
                val activeInvoices = invoiceRepository.countActiveInvoicesByParkingLotUuid(updatedParkingLot.parkingLotUuid)
                updatedParkingLot.availableSlots = req.capacity - activeInvoices.toInt()
            }

            // Update images
            if (req.images.isNullOrEmpty().not()) {
                parkingLotImageService.updateOrderOfImages(req.images)
            }

            // Save
            parkingLotService.saveParkingLot(updatedParkingLot)

            return ResponseEntity.ok(ApiResponse(null, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null)))
            )
        }
    }

    /**
     * Verify Parking Lot
     */
    @PutMapping("/verify/{parkingLotUuid}")
    fun verifyParkingLot(
        @PathVariable parkingLotUuid: String,
        principal: Principal
    ): ResponseEntity<ApiResponse<ParkingLot>> {
        return try {
            val parkingLot = parkingLotService.verifyParkingLot(principal.name, parkingLotUuid)
            ResponseEntity.ok(ApiResponse(parkingLot, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null)))
            )
        }
    }

    /**
     * Delete Image
     */
    @PostMapping("/delete-image/{imageId}")
    fun deleteImage(
        @PathVariable imageId: String
    ): ResponseEntity<ApiResponse<ParkingLotImage>> {
        return try {
            parkingLotImageService.deleteImage(imageId)
            ResponseEntity.ok(ApiResponse(null, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null)))
            )
        }
    }

    /**
     * Restore Image
     */
    @PostMapping("/restore-image/{imageId}")
    fun restoreImage(
        @PathVariable imageId: String
    ): ResponseEntity<ApiResponse<ParkingLotImage>> {
        return try {
            parkingLotImageService.restoreImage(imageId)
            ResponseEntity.ok(ApiResponse(null, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null)))
            )
        }
    }

    /**
     * Get parking lot avatar by image uuid
     */
    @GetMapping("/get-image/{imageUuid}")
    fun getParkingLotImage(
        @PathVariable imageUuid: String,
        response: HttpServletResponse
    ) {
        try {
            val image = parkingLotImageRepository.findByIdAndStatus(imageUuid, CloudFile.ACTIVE).get()
            if (image.cloudFile == null) {
                throw Exception("Image not found")
            }
            // Blob content type
            response.contentType = image.cloudFile.mimeType
            val inputStream = fileStorageService.downloadFile(image.cloudFile.key)
            inputStream.copyTo(response.outputStream)
            response.flushBuffer()
        } catch (e: Exception) {
            e.printStackTrace()
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
        }
    }
}