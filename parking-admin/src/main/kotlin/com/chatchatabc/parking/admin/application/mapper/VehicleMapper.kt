package com.chatchatabc.parking.admin.application.mapper

import com.chatchatabc.parking.admin.application.rest.VehicleController
import com.chatchatabc.parking.domain.model.Vehicle
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface VehicleMapper {
    /**
     * Update vehicle from request
     */
    @Mappings(
        Mapping(target = "name", source = "request.name"),
        Mapping(target = "plateNumber", source = "request.plateNumber"),
        Mapping(target = "type", source = "request.type")
    )
    fun updateVehicleFromUpdateRequest(
        request: VehicleController.VehicleUpdateRequest,
        @MappingTarget vehicle: Vehicle
    )
}