package com.chatchatabc.parking.api.application.mapper

import com.chatchatabc.parking.api.application.rest.ReportController
import com.chatchatabc.parking.domain.model.Report
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface ReportMapper {
    /**
     * Create Report from request
     */
    @Mappings(
        Mapping(target = "name", source = "request.name"),
        Mapping(target = "description", source = "request.description"),
        Mapping(target = "plateNumber", source = "request.plateNumber"),
        Mapping(target = "latitude", source = "request.latitude"),
        Mapping(target = "longitude", source = "request.longitude")
    )
    fun createReportFromRequest(
        request: ReportController.ReportCreateRequest,
        @MappingTarget report: Report
    )

    /**
     * Update Report from Request
     */
    @Mappings(
        Mapping(target = "name", source = "request.name"),
        Mapping(target = "description", source = "request.description"),
        Mapping(target = "plateNumber", source = "request.plateNumber"),
        Mapping(target = "latitude", source = "request.latitude"),
        Mapping(target = "longitude", source = "request.longitude")
    )
    fun updateReportFromRequest(
        request: ReportController.ReportUpdateRequest,
        @MappingTarget report: Report
    )
}