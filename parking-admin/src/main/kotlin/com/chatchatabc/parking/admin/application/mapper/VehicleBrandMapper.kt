package com.chatchatabc.parking.admin.application.mapper

import com.chatchatabc.parking.admin.application.rest.VehicleBrandController
import com.chatchatabc.parking.domain.model.VehicleBrand
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface VehicleBrandMapper {
    @Mappings(
        Mapping(target = "name", source = "request.name"),
        Mapping(target = "status", source = "request.status")
    )
    fun createVehicleBrandFromCreateRequest(
        request: VehicleBrandController.VehicleBrandCreateRequest,
        @MappingTarget vehicleBrand: VehicleBrand
    )
}