package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.api.application.dto.ErrorElement
import com.chatchatabc.parking.api.application.mapper.ParkingLotMapper
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.ParkingLot
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
import org.springframework.core.io.InputStreamResource
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/api/parking-lot")
class ParkingLotController(
    private val parkingLotService: ParkingLotService,
    private val parkingLotRepository: ParkingLotRepository,
    private val userRepository: UserRepository,
    private val parkingLotImageService: ParkingLotImageService,
    private val parkingLotImageRepository: ParkingLotImageRepository,
    private val fileStorageService: FileStorageService,
    private val invoiceRepository: InvoiceRepository
) {
    private val fileNamespace = "parkingLot"
    private val parkingLotMapper = Mappers.getMapper(ParkingLotMapper::class.java)

    /**
     * Get all parking lots
     */
    @GetMapping("/get-all")
    fun getAll(
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<ParkingLot>>> {
        return try {
            val parkingLots = parkingLotRepository.findByStatusGreaterThanEqual(ParkingLot.DRAFT, pageable)
            ResponseEntity.ok(ApiResponse(parkingLots, listOf()))
        } catch (e: Exception) {
            ResponseEntity.ok(
                ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_NOT_FOUND.name, null)))
            )
        }
    }

    /**
     * Get parking lots by uuid
     */
    @GetMapping("/get/{parkingLotUuid}")
    fun get(
        @PathVariable parkingLotUuid: String
    ): ResponseEntity<ApiResponse<ParkingLot>> {
        return try {
            val parkingLot = parkingLotRepository.findByParkingLotUuid(parkingLotUuid).get()
            ResponseEntity.ok(ApiResponse(parkingLot, listOf()))
        } catch (e: Exception) {
            ResponseEntity.ok(
                ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_NOT_FOUND.name, null)))
            )
        }
    }

    /**
     * Get Parking Lot by Owner
     */
    @GetMapping("/get")
    fun getByManaging(
        principal: Principal
    ): ResponseEntity<ApiResponse<ParkingLot>> {
        return try {
            val parkingLot = parkingLotRepository.findByOwnerUuid(principal.name).getOrNull()
            ResponseEntity.ok(ApiResponse(parkingLot, listOf()))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
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
            return ResponseEntity.ok(ApiResponse(parkingLots, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
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
                parkingLot.id,
                0,
                pageable
            )
            ResponseEntity.ok(ApiResponse(images, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }

    /**
     * Create parking lot data class
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
     * Register a parking lot
     */
    @PostMapping("/register")
    fun register(
        @RequestBody req: ParkingLotCreateRequest,
        principal: Principal
    ): ResponseEntity<ApiResponse<Nothing>> {
        return try {
            val owner = userRepository.findByUserUuid(principal.name).get()
            val createdParkingLot = ParkingLot()
            createdParkingLot.owner = owner.id
            createdParkingLot.availableSlots = req.capacity
            parkingLotMapper.createParkingLotFromCreateRequest(req, createdParkingLot)
            parkingLotService.saveParkingLot(createdParkingLot)
            return ResponseEntity.ok(ApiResponse(null, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_CREATE.name, null))))
        }
    }

    /**
     * Update parking lot data class
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
     * Update a parking lot and image order
     */
    @PutMapping("/update")
    fun update(
        @RequestBody req: ParkingLotUpdateRequest,
        principal: Principal
    ): ResponseEntity<ApiResponse<Nothing>> {
        return try {
            // Map request to parking lot
            val parkingLotUuid = parkingLotRepository.findByOwnerUuid(principal.name).get().parkingLotUuid
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

            return ResponseEntity.ok(ApiResponse(null, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_UPDATE.name, null))))
        }
    }

    /**
     * Set parking lot status to pending
     */
    @PutMapping("/set-pending")
    fun setPending(
        principal: Principal
    ): ResponseEntity<ApiResponse<ParkingLot>> {
        return try {
            val user = userRepository.findByUserUuid(principal.name).get()
            val parkingLot = parkingLotRepository.findByOwner(user.id).get()
            parkingLot.status = ParkingLot.PENDING
            parkingLotRepository.save(parkingLot)
            ResponseEntity.ok(ApiResponse(parkingLot, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_UPDATE.name, null))))
        }
    }

    /**
     * Get parking lot avatar by image id
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
    @PostMapping("/upload-image")
    fun uploadImage(
        @RequestParam("file", required = true) file: MultipartFile,
        principal: Principal
    ): ResponseEntity<ApiResponse<ParkingLotImage>> {
        return try {
            val user = userRepository.findByUserUuid(principal.name).get()
            val parkingLot = parkingLotRepository.findByOwner(user.id).get()
            val fileData = parkingLotImageService.uploadImage(
                user,
                parkingLot,
                fileNamespace,
                file.inputStream,
                file.originalFilename,
                file.size,
                file.contentType
            )
            return ResponseEntity.ok(ApiResponse(fileData, listOf()))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_UPDATE.name, null))))
        }
    }

    /**
     * Delete image
     */
    @PostMapping("/delete-image/{imageId}")
    fun deleteImage(
        @PathVariable imageId: String,
        principal: Principal
    ): ResponseEntity<ApiResponse<ParkingLotImage>> {
        return try {
            // Only owner can delete image
            val image = parkingLotImageRepository.findById(imageId).get()
            val currentUser = userRepository.findByUserUuid(principal.name).get()
            val parkingLot = parkingLotRepository.findById(image.parkingLot).get()
            if (parkingLot.owner != currentUser.id) {
                throw Exception("You are not owner of this parking lot")
            }
            parkingLotImageService.deleteImage(imageId)
            ResponseEntity.ok(ApiResponse(null, listOf()))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_UPDATE.name, null))))
        }
    }
}