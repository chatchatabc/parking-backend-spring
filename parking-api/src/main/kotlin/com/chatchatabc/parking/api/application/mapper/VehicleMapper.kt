package com.chatchatabc.parking.api.application.mapper

import com.chatchatabc.parking.api.application.rest.VehicleController
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
        Mapping(target = "brandUuid", source = "request.brandUuid"),
        Mapping(target = "modelUuid", source = "request.modelUuid"),
        Mapping(target = "typeUuid", source = "request.typeUuid"),
        Mapping(target = "color", source = "request.color"),
        Mapping(target = "year", source = "request.year"),
    )
    fun updateVehicleFromUpdateRequest(
        request: VehicleController.VehicleUpdateRequest,
        @MappingTarget vehicle: Vehicle
    )
}