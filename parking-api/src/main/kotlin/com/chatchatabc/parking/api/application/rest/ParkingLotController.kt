package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.api.application.dto.ParkingLotCreateRequest
import com.chatchatabc.parking.api.application.dto.ParkingLotUpdateRequest
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.model.file.ParkingLotImage
import com.chatchatabc.parking.domain.repository.MemberRepository
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.file.ParkingLotImageRepository
import com.chatchatabc.parking.domain.service.ParkingLotService
import com.chatchatabc.parking.domain.service.file.ParkingLotImageService
import com.chatchatabc.parking.infra.service.FileStorageService
import com.chatchatabc.parking.web.common.application.common.MemberPrincipal
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.io.InputStreamResource
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/api/parking-lot")
class ParkingLotController(
    private val parkingLotService: ParkingLotService,
    private val parkingLotRepository: ParkingLotRepository,
    private val memberRepository: MemberRepository,
    private val parkingLotImageService: ParkingLotImageService,
    private val parkingLotImageRepository: ParkingLotImageRepository,
    private val fileStorageService: FileStorageService

) {
    private val fileNamespace = "parkingLot"

    /**
     * Get parking lots by uuid
     */
    @GetMapping("/get/{parkingLotUuid}")
    fun get(
        @PathVariable parkingLotUuid: String
    ): ResponseEntity<ApiResponse<ParkingLot>> {
        return try {
            val parkingLot = parkingLotRepository.findByParkingLotUuid(parkingLotUuid).get()
            ResponseEntity.ok(ApiResponse(parkingLot, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false))
        } catch (e: Exception) {
            ResponseEntity.ok(
                ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR_NOT_FOUND.name, true)
            )
        }
    }

    /**
     * Get Parking Lot by Owner
     */
    @GetMapping("/get")
    fun getByManaging(
        principal: MemberPrincipal
    ): ResponseEntity<ApiResponse<ParkingLot>> {
        return try {
            val member = memberRepository.findByMemberUuid(principal.memberUuid).get()
            val parkingLot = parkingLotRepository.findByOwner(member.id).getOrNull()
            ResponseEntity.ok(
                ApiResponse(
                    parkingLot,
                    HttpStatus.OK.value(),
                    ResponseNames.SUCCESS.name,
                    false
                )
            )
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse(
                    null,
                    HttpStatus.BAD_REQUEST.value(),
                    ResponseNames.ERROR.name,
                    true
                )
            )
        }
    }

    /**
     * Get parking lots by distance
     */
    @GetMapping("/get-by-location")
    fun getByLocation(
        @RequestParam("longitude") longitude: Double,
        @RequestParam("latitude") latitude: Double,
        @RequestParam("distance") distance: Double,
    ): ResponseEntity<ApiResponse<List<ParkingLot>>> {
        return try {
            var inputDistance = distance
            // Place a cap on the range, distance should not exceed 0.1km
            if (distance >= 0.1) {
                inputDistance = 0.1
            }
            val parkingLots = parkingLotRepository.findByDistance(longitude, latitude, inputDistance)
            return ResponseEntity.ok(
                ApiResponse(
                    parkingLots,
                    HttpStatus.OK.value(),
                    ResponseNames.SUCCESS.name,
                    false
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(
                    null,
                    HttpStatus.BAD_REQUEST.value(),
                    ResponseNames.ERROR.name,
                    true
                )
            )
        }
    }

    /**
     * Get Images of a Parking Lot By Uuid and active status
     */
    @GetMapping("/get-images/{parkingLotUuid}")
    fun getImages(
        @PathVariable parkingLotUuid: String,
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<ParkingLotImage>>> {
        return try {
            val parkingLot = parkingLotRepository.findByParkingLotUuid(parkingLotUuid).get()
            val images = parkingLotImageRepository.findAllByParkingLotAndStatus(
                parkingLot,
                0,
                pageable
            )
            ResponseEntity.ok(
                ApiResponse(
                    images,
                    HttpStatus.OK.value(),
                    ResponseNames.SUCCESS.name,
                    false
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(
                    null,
                    HttpStatus.BAD_REQUEST.value(),
                    ResponseNames.ERROR.name,
                    true
                )
            )
        }
    }

    /**
     * Register a parking lot
     */
    @PostMapping("/register")
    fun register(
        @RequestBody req: ParkingLotCreateRequest,
        principal: MemberPrincipal
    ): ResponseEntity<ApiResponse<ParkingLot>> {
        return try {
            val createdParkingLot = parkingLotService.registerParkingLot(
                principal.memberUuid,
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
            return ResponseEntity.ok(
                ApiResponse(
                    createdParkingLot,
                    HttpStatus.OK.value(),
                    ResponseNames.SUCCESS_CREATE.name,
                    false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    ApiResponse(
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        ResponseNames.ERROR_CREATE.name,
                        true
                    )
                )
        }
    }

    /**
     * Update a parking lot and image order
     */
    @PutMapping("/update/{parkingLotId}")
    fun update(
        @RequestBody req: ParkingLotUpdateRequest,
        @PathVariable parkingLotId: String,
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
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    ApiResponse(
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        ResponseNames.ERROR_UPDATE.name,
                        true
                    )
                )
        }
    }

    /**
     * Set parking lot status to pending
     */
    @PutMapping("/set-pending")
    fun setPending(
        principal: MemberPrincipal
    ): ResponseEntity<ApiResponse<ParkingLot>> {
        return try {
            val member = memberRepository.findByMemberUuid(principal.memberUuid).get()
            val parkingLot = parkingLotRepository.findByOwner(member.id).get()
            parkingLot.status = 1
            parkingLotRepository.save(parkingLot)
            ResponseEntity.ok(
                ApiResponse(
                    parkingLot,
                    HttpStatus.OK.value(),
                    ResponseNames.SUCCESS_UPDATE.name,
                    false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    ApiResponse(
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        ResponseNames.ERROR_UPDATE.name,
                        true
                    )
                )
        }
    }

    /**
     * Get member avatar by image id
     */
    @GetMapping("/get-image/{imageId}")
    fun getParkingLotImage(
        @PathVariable imageId: String,
        response: HttpServletResponse
    ): ResponseEntity<InputStreamResource> {
        return try {
            val image = parkingLotImageRepository.findByIdAndStatus(imageId, 1).get()
            if (image.cloudFile == null) {
                throw Exception("Image not found")
            }
            val headers = HttpHeaders()
            val resource = InputStreamResource(fileStorageService.downloadFile(image.cloudFile.key))
            ResponseEntity<InputStreamResource>(resource, headers, HttpStatus.OK)
        } catch (e: Exception) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Upload image
     */
    @PostMapping("/upload-image/{parkingLotUuid}")
    fun uploadImage(
        @PathVariable parkingLotUuid: String,
        @RequestParam("file", required = true) file: MultipartFile,
        principal: MemberPrincipal
    ): ResponseEntity<ApiResponse<ParkingLotImage>> {
        return try {
            val member = memberRepository.findByMemberUuid(principal.memberUuid).get()
            val parkingLot = parkingLotRepository.findByParkingLotUuid(parkingLotUuid).get()
            val fileData = parkingLotImageService.uploadImage(
                member,
                parkingLot,
                fileNamespace,
                file.inputStream,
                file.originalFilename,
                file.size,
                file.contentType
            )
            return ResponseEntity.ok(
                ApiResponse(
                    fileData,
                    HttpStatus.OK.value(),
                    ResponseNames.SUCCESS_UPDATE.name,
                    false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    ApiResponse(
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        ResponseNames.ERROR_UPDATE.name,
                        true
                    )
                )
        }
    }

    /**
     * Delete image
     */
    @PostMapping("/delete-image/{imageId}")
    fun deleteImage(
        @PathVariable imageId: String,
        principal: MemberPrincipal
    ): ResponseEntity<ApiResponse<ParkingLotImage>> {
        return try {
            // Only owner can delete image
            val image = parkingLotImageRepository.findById(imageId).get()
            val currentMember = memberRepository.findByMemberUuid(principal.memberUuid).get()
            val parkingLot = parkingLotRepository.findById(image.parkingLot).get()
            if (parkingLot.owner != currentMember.id) {
                throw Exception("You are not owner of this parking lot")
            }
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
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    ApiResponse(
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        ResponseNames.ERROR_UPDATE.name,
                        true
                    )
                )
        }
    }
}