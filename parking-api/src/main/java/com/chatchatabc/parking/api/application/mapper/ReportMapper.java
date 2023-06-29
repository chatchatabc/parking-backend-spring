package com.chatchatabc.parking.api.application.mapper;

import com.chatchatabc.parking.domain.model.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    record ReportMapDTO(
            String name,
            String description,
            String plateNumber,
            double latitude,
            double longitude
    ) {
    }

    /**
     * Report Mapper
     */
    @Mappings({
            @Mapping(target = "name", source = "request.name"),
            @Mapping(target = "description", source = "request.description"),
            @Mapping(target = "plateNumber", source = "request.plateNumber"),
            @Mapping(target = "latitude", source = "request.latitude"),
            @Mapping(target = "longitude", source = "request.longitude")
    })
    void mapRequestToReport(
            ReportMapDTO request,
            @MappingTarget Report report
    );
}
