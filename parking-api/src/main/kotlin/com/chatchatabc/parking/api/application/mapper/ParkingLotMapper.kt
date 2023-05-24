package com.chatchatabc.parking.api.application.mapper

import com.chatchatabc.parking.api.application.rest.ParkingLotController
import com.chatchatabc.parking.domain.model.ParkingLot
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface ParkingLotMapper {

    /**
     * Parking Lot Mapper
     */
    @Mappings(
        Mapping(target = "name", source = "request.name"),
        Mapping(target = "latitude", source = "request.latitude"),
        Mapping(target = "longitude", source = "request.longitude"),
        Mapping(target = "address", source = "request.address"),
        Mapping(target = "description", source = "request.description"),
        Mapping(target = "capacity", source = "request.capacity"),
        Mapping(target = "businessHoursStart", source = "request.businessHoursStart"),
        Mapping(target = "businessHoursEnd", source = "request.businessHoursEnd"),
        Mapping(target = "openDaysFlag", source = "request.openDaysFlag"),
    )
    fun createParkingLotFromCreateRequest(
        request: ParkingLotController.ParkingLotCreateRequest,
        @MappingTarget parkingLot: ParkingLot
    )
}