package com.chatchatabc.parking.web.common.application.nats;

public class NatsPayload {
    public record InvoicePayload(
            String parkingLotUuid,
            String vehicleUuid,
            String plateNumber,
            String invoiceUuid
    ) {

    }

    public record JeepneyPayload(
            String jeepneyUuid,
            Double latitude,
            Double longitude,
            Integer direction
    ) {

    }
}