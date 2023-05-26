package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.admin.application.mapper.ParkingLotMapper
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.model.file.ParkingLotImage
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import com.chatchatabc.parking.domain.repository.MemberRepository
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.service.ParkingLotService
import com.chatchatabc.parking.domain.service.file.ParkingLotImageService
import org.mapstruct.factory.Mappers
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/parking-lot")
class ParkingLotController(
    private val parkingLotService: ParkingLotService,
    private val parkingLotImageService: ParkingLotImageService,
    private val parkingLotRepository: ParkingLotRepository,
    private val memberRepository: MemberRepository,
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
    @PostMapping("/create/{memberUuid}")
    fun createParkingLot(
        @PathVariable memberUuid: String,
        @RequestBody req: ParkingLotCreateRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        return try {
            val owner = memberRepository.findByMemberUuid(memberUuid).get()
            val createdParkingLot = ParkingLot()
            createdParkingLot.owner = owner.id
            parkingLotMapper.createParkingLotFromCreateRequest(req, createdParkingLot)
            parkingLotService.saveParkingLot(createdParkingLot)
            ResponseEntity.ok(ApiResponse(null, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true)
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
                val activeInvoices = invoiceRepository.countActiveInvoicesByParkingLotId(updatedParkingLot.id)
                updatedParkingLot.availableSlots = req.capacity - activeInvoices.toInt()
            }

            // Update images
            parkingLotImageService.updateOrderOfImages(req.images)

            // Save
            parkingLotService.saveParkingLot(updatedParkingLot)

            return ResponseEntity.ok(
                ApiResponse(
                    null,
                    HttpStatus.OK.value(),
                    ResponseNames.SUCCESS_UPDATE.name,
                    false
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true)
            )
        }
    }

    /**
     * Verify Parking Lot
     */
    @PutMapping("/verify/{parkingLotId}")
    fun verifyParkingLot(
        @PathVariable parkingLotId: String,
        principal: Principal
    ): ResponseEntity<ApiResponse<ParkingLot>> {
        return try {
            val parkingLot = parkingLotService.verifyParkingLot(principal.name, parkingLotId)
            ResponseEntity.ok(ApiResponse(parkingLot, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true)
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
            ResponseEntity.ok(
                ApiResponse(
                    null,
                    HttpStatus.OK.value(),
                    ResponseNames.SUCCESS_UPDATE.name,
                    false
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true)
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
            ResponseEntity.ok(
                ApiResponse(
                    null,
                    HttpStatus.OK.value(),
                    ResponseNames.SUCCESS_UPDATE.name,
                    false
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR_UPDATE.name, true)
            )
        }
    }
}