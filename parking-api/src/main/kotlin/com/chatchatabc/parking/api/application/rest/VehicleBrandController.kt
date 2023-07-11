package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.domain.model.VehicleBrand
import com.chatchatabc.parking.domain.repository.VehicleBrandRepository
import com.chatchatabc.parking.domain.vehicleBrand
import com.chatchatabc.parking.infra.service.FileStorageService
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/vehicle-brand")
class VehicleBrandController(
    private val vehicleBrandRepository: VehicleBrandRepository,
    private val fileStorageService: FileStorageService
) {
    /**
     * Get vehicle brands
     */
    @Operation(
        summary = "Get vehicle brands",
        description = "Allow users to get vehicle brands"
    )
    @GetMapping
    fun getAll(pageable: Pageable) =
        vehicleBrandRepository.findAllByStatus(VehicleBrand.VehicleBrandStatus.ACTIVE, pageable).toResponse()

    /**
     * Get vehicle brand by identifier
     */
    @Operation(
        summary = "Get vehicle brand by identifier",
        description = "Allow users to get vehicle brand by identifier"
    )
    @GetMapping("/{id}")
    fun get(@PathVariable id: String) = run { id.vehicleBrand.toResponse() }

    /**
     * Get Vehicle Brand Logo
     */
    @Operation(
        summary = "Get Vehicle Brand Logo",
        description = "Get Vehicle Brand Logo"
    )
    @GetMapping("/logo/{id}")
    fun getVehicleBrandLogo(
        @PathVariable id: String,
        response: HttpServletResponse
    ) = runCatching {
        val vehicleBrand = id.vehicleBrand
        // Add 1 day cache
        response.setHeader("Cache-Control", "max-age=86400")
        response.contentType = vehicleBrand.logo.mimeType
        val inputStream = fileStorageService.downloadFile(vehicleBrand.logo.key)
        inputStream.copyTo(response.outputStream)
        response.flushBuffer()
    }.getOrElse { response.sendError(HttpServletResponse.SC_NOT_FOUND) }
}