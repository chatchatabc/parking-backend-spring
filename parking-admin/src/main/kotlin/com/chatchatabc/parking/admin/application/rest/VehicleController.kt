package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.VehicleMapper
import com.chatchatabc.parking.domain.model.Vehicle
import com.chatchatabc.parking.domain.model.file.CloudFile
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.VehicleService
import com.chatchatabc.parking.domain.specification.VehicleSpecification
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.domain.vehicle
import com.chatchatabc.parking.infra.service.FileStorageService
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletResponse
import org.mapstruct.factory.Mappers
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/vehicle")
class VehicleController(
    private val vehicleService: VehicleService,
    private val fileStorageService: FileStorageService,
    private val vehicleRepository: VehicleRepository
) {
    private val vehicleMapper = Mappers.getMapper(VehicleMapper::class.java)

    /**
     * Get Vehicle by identifier
     */
    @Operation(
        summary = "Get Vehicle",
        description = "Get Vehicle"
    )
    @GetMapping("/{id}")
    fun getVehicle(@PathVariable id: String) = id.vehicle.toResponse()

    /**
     * Get Vehicles
     */
    @Operation(
        summary = "Get Vehicles",
        description = "Get Vehicles"
    )
    @GetMapping
    fun getVehicles(
        pageable: Pageable,
        @RequestParam params: Map<String, String>
    ) = run {
        val spec = VehicleSpecification()
            .withParams(params)

        // Search with Keyword
        if (params.containsKey("keyword")) {
            val keyword = params["keyword"]
            spec.or(VehicleSpecification().withKeyword(keyword))
        }

        vehicleRepository.findAll(spec, pageable).toResponse()
    }

    /**
     * Get Vehicles by Owner
     */
    @Operation(
        summary = "Get Vehicles by Owner",
        description = "Get Vehicles by Owner"
    )
    @GetMapping("/owner/{id}")
    fun getVehiclesByOwner(
        @PathVariable id: String,
        pageable: Pageable
    ) = run {
        vehicleRepository.findAllByOwner(id.user.id, pageable).toResponse()
    }

    /**
     * Get Owner by Vehicle identifier
     */
    @Operation(
        summary = "Get Owner by Vehicle",
        description = "Get Owner by Vehicle"
    )
    @GetMapping("/vehicle-user/{id}")
    fun getUserByVehicle(@PathVariable id: String) = run { id.vehicle.owner.user.toResponse() }

    /**
     * Request to register a vehicle data class
     */
    data class VehicleRegisterRequest(
        val name: String,
        val plateNumber: String,
        val modelUuid: String,
        val color: String,
        val year: String
    )

    /**
     * Register a vehicle
     */
    @Operation(
        summary = "Register Vehicle",
        description = "Register Vehicle"
    )
    @PostMapping("/{userUuid}")
    fun registerVehicle(
        @PathVariable userUuid: String,
        @RequestBody req: VehicleRegisterRequest
    ) = runCatching {
        vehicleService.registerVehicle(
            userUuid, req.name,
            req.plateNumber,
            req.modelUuid,
            req.color,
            req.year
        ).toResponse()
    }.getOrElse { it.toErrorResponse() }

    data class VehicleVerifyRequest(
        val status: Int = Vehicle.VehicleStatus.DRAFT,
        val rejectionReason: String?
    )

    /**
     * Verify a vehicle
     */
    @Operation(
        summary = "Verify Vehicle",
        description = "Verify Vehicle"
    )
    @PutMapping("/verify/{id}")
    fun verifyVehicle(
        @PathVariable id: String,
        principal: Principal,
        @RequestBody req: VehicleVerifyRequest
    ) = runCatching {
        val vehicle = id.vehicle
        vehicle.status = req.status
        if (req.rejectionReason != null) {
            vehicle.rejectionReason = req.rejectionReason
        }
        if (req.status == Vehicle.VehicleStatus.VERIFIED) {
            vehicle.verifiedBy = principal.name.user.id
            vehicle.verifiedAt = LocalDateTime.now()
        }
        vehicleService.saveVehicle(vehicle).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Update a vehicle
     */
    @Operation(
        summary = "Update Vehicle",
        description = "Update Vehicle"
    )
    @PutMapping("/{vehicleUuid}")
    fun updateVehicle(
        @PathVariable vehicleUuid: String,
        @RequestBody req: VehicleMapper.VehicleMapDTO,
        principal: Principal
    ) = runCatching {
        val vehicle = vehicleUuid.vehicle
        vehicleMapper.mapRequestToVehicle(req, vehicle)
        vehicleService.updateVehicle(vehicle).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Get Vehicle Image
     */
    @Operation(
        summary = "Get Vehicle Image",
        description = "Get Vehicle Image. Side value: front, back, left, right"
    )
    @GetMapping("/image/{id}/{side}")
    fun getVehicleImage(
        @PathVariable id: String,
        @PathVariable side: String,
        principal: Principal,
        response: HttpServletResponse
    ) = runCatching {
        val vehicle = id.vehicle
        var cloudFile: CloudFile? = null

        when (side) {
            "front" -> cloudFile = vehicle.imageFront
            "back" -> cloudFile = vehicle.imageBack
            "left" -> cloudFile = vehicle.imageLeft
            "right" -> cloudFile = vehicle.imageRight
        }

        if (cloudFile == null) {
            throw Exception("Vehicle image not found")
        }
        response.contentType = cloudFile.mimeType
        // Add 1 day cache
        response.setHeader("Cache-Control", "max-age=86400")
        val inputStream = fileStorageService.downloadFile(cloudFile.key)
        inputStream.copyTo(response.outputStream)
        response.flushBuffer()
    }.getOrElse { response.sendError(HttpServletResponse.SC_NOT_FOUND) }
}