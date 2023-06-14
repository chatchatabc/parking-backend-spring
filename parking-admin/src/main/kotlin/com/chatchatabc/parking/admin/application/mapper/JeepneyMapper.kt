package com.chatchatabc.parking.admin.application.mapper

import com.chatchatabc.parking.admin.application.rest.JeepneyController
import com.chatchatabc.parking.domain.model.Jeepney
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface JeepneyMapper {

    /**
     * Jeepney Create Mapper
     */
    @Mappings(
        Mapping(target = "name", source = "request.name"),
        Mapping(target = "plateNumber", source = "request.plateNumber"),
        Mapping(target = "capacity", source = "request.capacity"),
        Mapping(target = "latitude", source = "request.latitude"),
        Mapping(target = "longitude", source = "request.longitude"),
        Mapping(target = "status", source = "request.status"),
        Mapping(target = "flag", source = "request.flag"),
    )
    fun createJeepneyFromCreateRequest(
        request: JeepneyController.JeepneyCreateRequest,
        @MappingTarget jeepney: Jeepney
    )

    /**
     * Jeepney Update Mapper
     */
    @Mappings(
        Mapping(target = "name", source = "request.name"),
        Mapping(target = "plateNumber", source = "request.plateNumber"),
        Mapping(target = "capacity", source = "request.capacity"),
        Mapping(target = "latitude", source = "request.latitude"),
        Mapping(target = "longitude", source = "request.longitude"),
        Mapping(target = "status", source = "request.status"),
        Mapping(target = "flag", source = "request.flag"),
    )
    fun updateJeepneyFromUpdateRequest(
        request: JeepneyController.JeepneyUpdateRequest,
        @MappingTarget jeepney: Jeepney
    )
}