package com.chatchatabc.parking.admin.application.mapper;

import com.chatchatabc.parking.domain.model.VehicleModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface VehicleModelMapper {
    record VehicleModelResponse(
            String brandUuid,
            String name,
            int status
    ) {
    }

    /**
     * Map Vehicle Model From Request
     */
    @Mappings({
            @Mapping(target = "brandUuid", source = "request.brandUuid"),
            @Mapping(target = "name", source = "request.name"),
            @Mapping(target = "status", source = "request.status")
    })
    void mapRequestToVehicleModel(
            VehicleModelResponse request,
            @MappingTarget VehicleModel vehicleModel
    );
}
