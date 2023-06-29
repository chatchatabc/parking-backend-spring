package com.chatchatabc.parking.api.application.mapper;

import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.model.file.ParkingLotImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ParkingLotMapper {
    record ParkingLotMapDTO(
            String name,
            double latitude,
            double longitude,
            String address,
            String description,
            int capacity,
            LocalDateTime businessHoursStart,
            LocalDateTime businessHoursEnd,
            int openDaysFlag,
            List<ParkingLotImage> images
    ) {
    }

    /**
     * Parking Lot Mapper
     */
    @Mappings({
            @Mapping(target = "name", source = "request.name"),
            @Mapping(target = "latitude", source = "request.latitude"),
            @Mapping(target = "longitude", source = "request.longitude"),
            @Mapping(target = "address", source = "request.address"),
            @Mapping(target = "description", source = "request.description"),
            @Mapping(target = "capacity", source = "request.capacity"),
            @Mapping(target = "businessHoursStart", source = "request.businessHoursStart"),
            @Mapping(target = "businessHoursEnd", source = "request.businessHoursEnd"),
            @Mapping(target = "openDaysFlag", source = "request.openDaysFlag"),
    })
    void mapRequestToParkingLot(
            ParkingLotMapDTO request,
            @MappingTarget ParkingLot parkingLot
    );
}
