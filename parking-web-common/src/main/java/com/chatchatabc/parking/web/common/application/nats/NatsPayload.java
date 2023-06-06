package com.chatchatabc.parking.web.common.application.nats;

public class NatsPayload {
    public record InvoicePayload(
            String parkingLotUuid,
            String vehicleUuid,
            String invoiceUuid
    ) {

    }
}