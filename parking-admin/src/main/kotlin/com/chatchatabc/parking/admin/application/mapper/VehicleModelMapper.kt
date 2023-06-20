package com.chatchatabc.parking.admin.application.mapper

import com.chatchatabc.parking.admin.application.rest.VehicleModelController
import com.chatchatabc.parking.domain.model.VehicleModel
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface VehicleModelMapper {
    /**
     * Create vehicle model from request
     */
    @Mappings(
        Mapping(target = "brandUuid", source = "request.brandUuid"),
        Mapping(target = "name", source = "request.name"),
        Mapping(target = "status", source = "request.status")
    )
    fun createVehicleModelFromCreateRequest(
        request: VehicleModelController.VehicleModelCreateRequest,
        @MappingTarget vehicleModel: VehicleModel
    ): VehicleModel

    /**
     * Update vehicle model from request
     */
    @Mappings(
        Mapping(target = "brandUuid", source = "request.brandUuid"),
        Mapping(target = "name", source = "request.name"),
        Mapping(target = "status", source = "request.status")
    )
    fun updateVehicleModelFromUpdateRequest(
        request: VehicleModelController.VehicleModelUpdateRequest,
        @MappingTarget vehicleModel: VehicleModel
    ): VehicleModel
}