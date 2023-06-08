package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.admin.application.dto.ErrorElement
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Vehicle
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.VehicleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/vehicle")
class VehicleController(
    private val vehicleRepository: VehicleRepository,
    private val vehicleService: VehicleService
) {

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
}