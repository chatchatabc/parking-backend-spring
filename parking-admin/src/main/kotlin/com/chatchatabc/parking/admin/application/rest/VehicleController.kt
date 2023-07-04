package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.VehicleMapper
import com.chatchatabc.parking.domain.model.Vehicle
import com.chatchatabc.parking.domain.model.file.CloudFile
import com.chatchatabc.parking.domain.service.VehicleService
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.domain.vehicle
import com.chatchatabc.parking.infra.service.FileStorageService
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletResponse
import org.mapstruct.factory.Mappers
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/vehicle")
class VehicleController(
    private val vehicleService: VehicleService,
    private val fileStorageService: FileStorageService
) {
    private val vehicleMapper = Mappers.getMapper(VehicleMapper::class.java)

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
        description = "Get Vehicle Image. Side value: Front = 1, Back = 2, Left = 3, Right = 4"
    )
    @GetMapping("/image/{id}/{side}")
    fun getVehicleImage(
        @PathVariable id: String,
        @PathVariable side: Int,
        principal: Principal,
        response: HttpServletResponse
    ) = runCatching {
        val vehicle = id.vehicle
        var cloudFile: CloudFile? = null

        when (side) {
            Vehicle.VehicleImageType.FRONT -> cloudFile = vehicle.imageFront
            Vehicle.VehicleImageType.BACK -> cloudFile = vehicle.imageBack
            Vehicle.VehicleImageType.LEFT -> cloudFile = vehicle.imageLeft
            Vehicle.VehicleImageType.RIGHT -> cloudFile = vehicle.imageRight
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