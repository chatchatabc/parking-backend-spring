package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.admin.application.dto.ErrorElement
import com.chatchatabc.parking.admin.application.mapper.VehicleMapper
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Vehicle
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.VehicleService
import org.mapstruct.factory.Mappers
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/vehicle")
class VehicleController(
    private val vehicleRepository: VehicleRepository,
    private val vehicleService: VehicleService
) {
    private val vehicleMapper = Mappers.getMapper(VehicleMapper::class.java)

    /**
     * Request to register a vehicle data class
     */
    data class VehicleRegisterRequest(
        val name: String,
        val plateNumber: String,
        val type: Int,
    )

    /**
     * Register a vehicle
     */
    @PostMapping("/register/{userUuid}")
    fun registerVehicle(
        @PathVariable userUuid: String,
        @RequestBody req: VehicleRegisterRequest

    ): ResponseEntity<ApiResponse<Vehicle>> {
        return try {
            val vehicle = vehicleService.registerVehicle(userUuid, req.name, req.plateNumber, req.type)
            ResponseEntity.ok(ApiResponse(vehicle, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_CREATE.name, null))))
        }
    }

    /**
     * Update vehicle request
     */
    data class VehicleUpdateRequest(
        val name: String?,
        val plateNumber: String?,
        val type: Int?,
    )

    /**
     * Update a vehicle
     */
    @PutMapping("/update/{vehicleUuid}")
    fun updateVehicle(
        @PathVariable vehicleUuid: String,
        @RequestBody req: VehicleUpdateRequest,
        principal: Principal
    ): ResponseEntity<ApiResponse<Nothing>> {
        return try {
            val vehicle = vehicleRepository.findByVehicleUuid(vehicleUuid).get()
            vehicleMapper.updateVehicleFromUpdateRequest(req, vehicle)
            vehicleService.updateVehicle(vehicle)
            ResponseEntity.ok(ApiResponse(null, listOf()))
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_UPDATE.name, null))))
        }
    }
}