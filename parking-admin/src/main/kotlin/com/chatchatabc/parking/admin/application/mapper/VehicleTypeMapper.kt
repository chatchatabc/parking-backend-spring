package com.chatchatabc.parking.admin.application.mapper

import com.chatchatabc.parking.admin.application.rest.VehicleTypeController
import com.chatchatabc.parking.domain.model.VehicleType
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface VehicleTypeMapper {
    /**
     * Create vehicle type from request
     */
    @Mappings(
        Mapping(target = "name", source = "request.name"),
        Mapping(target = "status", source = "request.status")
    )
    fun createVehicleTypeFromCreateRequest(
        request: VehicleTypeController.VehicleTypeCreateRequest,
        @MappingTarget vehicleType: VehicleType
    ): VehicleType

    /**
     * Update vehicle type from request
     */
    @Mappings(
        Mapping(target = "name", source = "request.name"),
        Mapping(target = "status", source = "request.status")
    )
    fun updateVehicleTypeFromUpdateRequest(
        request: VehicleTypeController.VehicleTypeUpdateRequest,
        @MappingTarget vehicleType: VehicleType
    ): VehicleType
}