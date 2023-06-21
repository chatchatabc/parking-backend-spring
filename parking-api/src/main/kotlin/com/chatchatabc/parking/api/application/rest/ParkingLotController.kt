package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.mapper.ParkingLotMapper
import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.model.file.CloudFile
import com.chatchatabc.parking.domain.model.file.ParkingLotImage
import com.chatchatabc.parking.domain.parkingLot
import com.chatchatabc.parking.domain.parkingLotByOwner
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
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
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/parking-lot")
class ParkingLotController(
    private val parkingLotService: ParkingLotService,
    private val parkingLotRepository: ParkingLotRepository,
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
    @Operation(
        summary = "Get all parking lots",
        description = "Allow users to get all parking lots"
    )
    @GetMapping("/")
    fun getAll(pageable: Pageable) =
        parkingLotRepository.findByStatusGreaterThanEqual(ParkingLot.DRAFT, pageable).toResponse()

    /**
     * Get parking lot by uuid
     */
    @Operation(
        summary = "Get all parking lot by uuid",
        description = "Allow users to get parking lot by uuid"
    )
    @GetMapping("/{parkingLotUuid}")
    fun get(@PathVariable parkingLotUuid: String) = runCatching { parkingLotUuid.parkingLot.toResponse() }
        .getOrElse { it.toErrorResponse() }

    /**
     * Get Parking Lot by Owner
     */
    @Operation(
        summary = "Get all parking lot by owner",
        description = "Allow users to get parking lot by owner"
    )
    @GetMapping("/me")
    fun getByManaging(principal: Principal) = runCatching { principal.name.parkingLotByOwner.toResponse() }
        .getOrElse { it.toErrorResponse() }

    /**
     * Get parking lots by distance
     */
    @Operation(
        summary = "Get all parking lots by location",
        description = "Allow users to get parking lots by location"
    )
    @GetMapping("/location")
    fun getByLocation(
        @RequestParam("longitude") longitude: Double,
        @RequestParam("latitude") latitude: Double,
        @RequestParam("distance") distance: Double,
    ) = run {
        val cappedDistance = if (distance >= 0.1) 0.1 else distance
        parkingLotRepository.findByDistance(longitude, latitude, cappedDistance).toResponse()
    }

    /**
     * Get Images of a Parking Lot By Uuid and active status
     */
    @Operation(
        summary = "Get all images of a parking lot by uuid",
        description = "Allow users to get images of a parking lot by uuid"
    )
    @GetMapping("/images/{parkingLotUuid}")
    fun getImages(
        @PathVariable parkingLotUuid: String,
        pageable: Pageable
    ) = runCatching {
        parkingLotImageRepository.findAllByParkingLotAndStatus(
            parkingLotUuid.parkingLot.id, 0, pageable
        ).toResponse()
    }.getOrElse { it.toErrorResponse() }

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
    @Operation(
        summary = "Register a parking lot",
        description = "Allow users to register a parking lot"
    )
    @PostMapping("/")
    fun register(
        @RequestBody req: ParkingLotCreateRequest,
        principal: Principal
    ) = runCatching {
        val owner = principal.name.user
        val createdParkingLot = ParkingLot().apply {
            this.owner = owner.id
            this.availableSlots = req.capacity
        }
        parkingLotMapper.createParkingLotFromCreateRequest(req, createdParkingLot)
        parkingLotService.saveParkingLot(createdParkingLot).toResponse()
    }.getOrElse { it.toErrorResponse() }

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
    @Operation(
        summary = "Update a parking lot",
        description = "Allow users to update a parking lot"
    )
    @PutMapping("/")
    fun update(
        @RequestBody req: ParkingLotUpdateRequest,
        principal: Principal
    ) = runCatching {
        // Map request to parking lot
        val parkingLot = principal.name.parkingLotByOwner
        parkingLotMapper.updateParkingLotFromUpdateRequest(req, parkingLot)
        parkingLot.openDaysFlag = req.openDaysFlag ?: 0

        // Update available slots if there are active invoices and if capacity is updated
        if (req.capacity != null) {
            val activeInvoices = invoiceRepository.countActiveInvoicesByParkingLotUuid(parkingLot.parkingLotUuid)
            parkingLot.availableSlots = req.capacity - activeInvoices.toInt()
        }
        if (req.images != null) {
            // Update images
            parkingLotImageService.updateOrderOfImages(req.images)
        }
        // Save
        parkingLotService.saveParkingLot(parkingLot).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Set parking lot status to pending
     */
    @Operation(
        summary = "Set parking lot status to pending",
        description = "Allow users to set parking lot status to pending"
    )
    @PutMapping("/set-pending")
    fun setPending(
        principal: Principal
    ) = runCatching {
        val user = principal.name.user
        val parkingLot = user.id.parkingLotByOwner.apply {
            this.status = ParkingLot.PENDING
        }
        parkingLotService.saveParkingLot(parkingLot).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Get parking lot avatar by image uuid
     */
    @Operation(
        summary = "Get parking lot avatar by image uuid",
        description = "Allow users to get parking lot avatar by image uuid"
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
    }.getOrElse { response.sendError(HttpServletResponse.SC_NOT_FOUND) }

    /**
     * Get featured parking lot image if exists
     */
    @Operation(
        summary = "Get featured parking lot image if exists",
        description = "Allow users to get featured parking lot image if exists"
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
    }.getOrElse { response.sendError(HttpServletResponse.SC_NOT_FOUND) }

    /**
     * Upload image
     */
    @Operation(
        summary = "Upload image",
        description = "Allow users to upload image"
    )
    @PostMapping("/upload-image")
    fun uploadImage(
        @RequestParam("file", required = true) file: MultipartFile,
        principal: Principal
    ) = runCatching {
        val user = principal.name.user
        val parkingLot = user.id.parkingLotByOwner
        var contentType = file.contentType
        if (contentType == "image/jpg") {
            contentType = "image/jpeg"
        }
        parkingLotImageService.uploadImage(
            user,
            parkingLot,
            fileNamespace,
            file.inputStream,
            file.originalFilename,
            file.size,
            contentType
        ).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Delete image
     */
    @Operation(
        summary = "Delete image",
        description = "Allow users to delete image"
    )
    @PostMapping("/delete-image/{imageUuid}")
    fun deleteImage(
        @PathVariable imageUuid: String,
        principal: Principal
    ) = runCatching {
        // Only owner can delete image
        val image = parkingLotImageRepository.findById(imageUuid).get()
        val currentUser = principal.name.user
        val parkingLot = image.parkingLot.parkingLot
        if (parkingLot.owner != currentUser.id) {
            throw Exception("You are not owner of this parking lot")
        }
        parkingLotImageService.deleteImage(imageUuid).toResponse()
    }.getOrElse { it.toErrorResponse() }
}