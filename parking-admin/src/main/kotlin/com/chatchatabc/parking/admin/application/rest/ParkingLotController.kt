package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.admin.application.dto.ParkingLotUpdateRequest
import com.chatchatabc.parking.admin.application.mapper.ParkingLotMapper
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.model.file.ParkingLotImage
import com.chatchatabc.parking.domain.repository.MemberRepository
import com.chatchatabc.parking.domain.service.ParkingLotService
import com.chatchatabc.parking.domain.service.file.ParkingLotImageService
import com.chatchatabc.parking.web.common.application.common.MemberPrincipal
import org.mapstruct.factory.Mappers
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/parking-lot")
class ParkingLotController(
    private val parkingLotService: ParkingLotService,
    private val parkingLotImageService: ParkingLotImageService,
    private val memberRepository: MemberRepository
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
     * Admin update Parking Lot
     */
    @PutMapping("/update/{parkingLotId}")
    fun updateParkingLot(
        @PathVariable parkingLotId: String,
        @RequestBody req: ParkingLotUpdateRequest,
        principal: MemberPrincipal
    ): ResponseEntity<ApiResponse<ParkingLot>> {
        return try {
            val updatedParkingLot = parkingLotService.updateParkingLot(
                principal.memberUuid,
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
        @PathVariable parkingLotId: String,
        principal: MemberPrincipal
    ): ResponseEntity<ApiResponse<ParkingLot>> {
        return try {
            val parkingLot = parkingLotService.verifyParkingLot(principal.memberUuid, parkingLotId)
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