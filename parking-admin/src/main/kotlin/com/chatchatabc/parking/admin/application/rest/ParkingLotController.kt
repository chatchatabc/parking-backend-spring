package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.admin.application.dto.parking_lot.ParkingLotCreateRequest
import com.chatchatabc.parking.admin.application.dto.parking_lot.ParkingLotUpdateRequest
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Member
import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.model.file.ParkingLotImage
import com.chatchatabc.parking.domain.service.ParkingLotService
import com.chatchatabc.parking.domain.service.service.ParkingLotImageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/parking-lot")
class ParkingLotController(
    private val parkingLotService: ParkingLotService,
    private val parkingLotImageService: ParkingLotImageService,
) {

    /**
     * Admin create Parking Lot
     */
    @PostMapping("/create/{memberId}")
    fun createParkingLot(
        @PathVariable memberId: String,
        @RequestBody req: ParkingLotCreateRequest
    ): ResponseEntity<ApiResponse<ParkingLot>> {
        return try {
            val createdParkingLot = parkingLotService.registerParkingLot(
                memberId,
                req.name,
                req.latitude,
                req.longitude,
                req.address,
                req.description,
                req.capacity,
                req.businessHoursStart,
                req.businessHoursEnd,
                req.openDaysFlag
            )
            ResponseEntity.ok(ApiResponse(createdParkingLot, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true)
            )
        }
    }

    /**
     * Admin update Parking Lot
     */
    @PutMapping("/update/{parkingLotId}")
    fun updateParkingLot(
        @PathVariable parkingLotId: String,
        @RequestBody req: ParkingLotUpdateRequest
    ): ResponseEntity<ApiResponse<ParkingLot>> {
        return try {
            // Get principal from Security Context
            val principal = SecurityContextHolder.getContext().authentication.principal as Member
            val updatedParkingLot = parkingLotService.updateParkingLot(
                principal.memberId,
                parkingLotId,
                req.name,
                req.latitude,
                req.longitude,
                req.address,
                req.description,
                req.capacity,
                req.businessHoursStart,
                req.businessHoursEnd,
                req.openDaysFlag,
                req.images
            )
            return ResponseEntity.ok(
                ApiResponse(
                    updatedParkingLot,
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
        @PathVariable parkingLotId: String
    ): ResponseEntity<ApiResponse<ParkingLot>> {
        return try {
            // Get Member from Security Context Holder
            val principal = SecurityContextHolder.getContext().authentication.principal as Member
            val parkingLot = parkingLotService.verifyParkingLot(principal.memberId, parkingLotId)
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