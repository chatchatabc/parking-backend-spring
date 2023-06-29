package com.chatchatabc.parking.api.application.mapper;

import com.chatchatabc.parking.domain.model.Rate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface RateMapper {

    record RateMapDTO(
            int type,
            int interval,
            int freeHours,
            boolean payForFreeHoursWhenExceeding,
            BigDecimal startingRate,
            BigDecimal rate
    ) {
    }

    /**
     * Map Rate from Request
     */
    @Mappings({
            @Mapping(target = "type", source = "request.type"),
            @Mapping(target = "interval", source = "request.interval"),
            @Mapping(target = "freeHours", source = "request.freeHours"),
            @Mapping(target = "payForFreeHoursWhenExceeding", source = "request.payForFreeHoursWhenExceeding"),
            @Mapping(target = "startingRate", source = "request.startingRate"),
            @Mapping(target = "rate", source = "request.rate")
    })
    void mapRequestToRate(
            RateMapDTO request,
            @MappingTarget Rate rate
    );
}
