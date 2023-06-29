package com.chatchatabc.parking.admin.application.mapper;

import com.chatchatabc.parking.domain.model.VehicleBrand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface VehicleBrandMapper {
    record VehicleBrandMapDTO(
            String name,
            int status
    ) {
    }

    /**
     * Map Vehicle Brand from Request
     */
    @Mappings({
            @Mapping(target = "name", source = "request.name"),
            @Mapping(target = "status", source = "request.status")
    })
    void mapRequestToVehicleBrand(
            VehicleBrandMapDTO request,
            @MappingTarget VehicleBrand vehicleBrand
    );
}
