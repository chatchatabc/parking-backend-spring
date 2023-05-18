package com.chatchatabc.parking.admin.application.mapper

import com.chatchatabc.parking.admin.application.rest.RateController
import com.chatchatabc.parking.domain.model.Rate
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface RateMapper {

    /**
     * Update rate from request
     */
    @Mappings(
        Mapping(target = "type", source = "request.type"),
        Mapping(target = "interval", source = "request.interval"),
        Mapping(target = "freeHours", source = "request.freeHours"),
        Mapping(target = "payForFreeHoursWhenExceeding", source = "request.payForFreeHoursWhenExceeding"),
        Mapping(target = "startingRate", source = "request.startingRate"),
        Mapping(target = "rate", source = "request.rate")
    )
    fun updateRateFromUpdateRateRequest(
        request: RateController.RateUpdateRequest,
        @MappingTarget rate: Rate
    )
}