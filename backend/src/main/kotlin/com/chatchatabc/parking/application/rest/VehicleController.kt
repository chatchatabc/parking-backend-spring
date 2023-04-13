package com.chatchatabc.parking.application.rest

import com.chatchatabc.parking.application.dto.ErrorContent
import com.chatchatabc.parking.application.dto.VehicleRegisterRequest
import com.chatchatabc.parking.application.dto.VehicleResponse
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.model.Vehicle
import com.chatchatabc.parking.domain.service.VehicleService
import org.modelmapper.ModelMapper
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/vehicle")
class VehicleController(
    private val vehicleService: VehicleService
) {
    private val mapper = ModelMapper()

    /**
     * Register a vehicle to a user
     */
    @PostMapping("/register")
    fun register(
        @RequestBody request: VehicleRegisterRequest
    ): ResponseEntity<VehicleResponse> {
        return try {
            // Get Principal from Security Context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val vehicle = mapper.map(request, Vehicle::class.java)
            val createdVehicle = vehicleService.register(principal.id, vehicle)
            ResponseEntity.ok().body(VehicleResponse(createdVehicle, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(VehicleResponse(null, ErrorContent("Vehicle Register Error", e.message ?: "Unknown Error")))
        }
    }
}