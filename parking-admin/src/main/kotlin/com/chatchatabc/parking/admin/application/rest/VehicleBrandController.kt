package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.VehicleBrandMapper
import com.chatchatabc.parking.domain.model.VehicleBrand
import com.chatchatabc.parking.domain.service.VehicleBrandService
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.domain.vehicleBrand
import com.chatchatabc.parking.infra.service.FileStorageService
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletResponse
import org.mapstruct.factory.Mappers
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@RestController
@RequestMapping("/api/vehicle-brand")
class VehicleBrandController(
    private val vehicleBrandService: VehicleBrandService,
    private val fileStorageService: FileStorageService
) {
    private val vehicleBrandMapper = Mappers.getMapper(VehicleBrandMapper::class.java)
    private val namespace = "vehicle-brand"

    /**
     * Admin create Vehicle Brand
     */
    @Operation(
        summary = "Create Vehicle Brand",
        description = "Create Vehicle Brand"
    )
    @PostMapping
    fun createVehicleBrand(
        @RequestBody req: VehicleBrandMapper.VehicleBrandMapDTO,
        principal: Principal
    ) = runCatching {
        val createdVehicleBrand = VehicleBrand().apply {
            this.createdBy = principal.name.user.id
        }
        vehicleBrandMapper.mapRequestToVehicleBrand(req, createdVehicleBrand)
        vehicleBrandService.saveVehicleBrand(createdVehicleBrand)
    }.getOrElse { it.toErrorResponse() }

    /**
     * Admin update Vehicle Brand
     */
    @Operation(
        summary = "Update Vehicle Brand",
        description = "Update Vehicle Brand"
    )
    @PutMapping("/{id}")
    fun updateVehicleBrand(
        @RequestBody req: VehicleBrandMapper.VehicleBrandMapDTO,
        principal: Principal,
        @PathVariable id: String
    ) = runCatching {
        val updatedVehicleBrand = id.vehicleBrand
        vehicleBrandMapper.mapRequestToVehicleBrand(req, updatedVehicleBrand)
        vehicleBrandService.saveVehicleBrand(updatedVehicleBrand)
    }.getOrElse { it.toErrorResponse() }

    /**
     * Update Vehicle Brand Logo
     */
    @Operation(
        summary = "Update Vehicle Brand Logo",
        description = "Update Vehicle Brand Logo"
    )
    @PostMapping("/upload-logo/{id}")
    fun uploadVehicleBrandLogo(
        @RequestParam("file") file: MultipartFile,
        @PathVariable id: String,
        principal: Principal
    ) = runCatching {
        val vehicleBrand = id.vehicleBrand
        var contentType = file.contentType
        if (contentType == "image/jpg") {
            contentType = "image/jpeg"
        }

        vehicleBrand.logo = fileStorageService.uploadFile(
            principal.name.user.id,
            namespace,
            file.inputStream,
            file.originalFilename,
            file.size,
            contentType
        )
        vehicleBrandService.saveVehicleBrand(vehicleBrand).toResponse()
    }.getOrElse { it.toErrorResponse() }

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