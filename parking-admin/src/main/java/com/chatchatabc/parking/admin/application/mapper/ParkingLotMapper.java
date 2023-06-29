package com.chatchatabc.parking.admin.application.mapper;

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
            String parkingLotUuid,
            String name,
            double latitude,
            double longitude,
            String address,
            String description,
            int capacity,
            int availableSlots,
            LocalDateTime businessHoursStart,
            LocalDateTime businessHoursEnd,
            Integer openDaysFlag,
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
            @Mapping(target = "availableSlots", source = "request.availableSlots"),
            @Mapping(target = "businessHoursStart", source = "request.businessHoursStart"),
            @Mapping(target = "businessHoursEnd", source = "request.businessHoursEnd"),
            @Mapping(target = "openDaysFlag", source = "request.openDaysFlag"),
    })
    void mapRequestToParkingLot(
            ParkingLotMapDTO request,
            @MappingTarget ParkingLot parkingLot
    );
}
