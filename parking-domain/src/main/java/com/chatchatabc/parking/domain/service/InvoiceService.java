package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.model.Invoice;

public interface InvoiceService {

    /**
     * Create invoice for vehicle
     *
     * @param parkingLotUuid                  the parking lot uuid
     * @param vehicleUuid                     the vehicle uuid
     * @param estimatedParkingDurationInHours the estimated parking duration in hours
     */
    Invoice createInvoice(String parkingLotUuid, String vehicleUuid, Integer estimatedParkingDurationInHours) throws Exception;

    /**
     * End invoice
     *
     * @param invoiceUuid    the invoice uuid
     * @param parkingLotUuid the parking lot uuid
     */
    void endInvoice(String invoiceUuid, String parkingLotUuid) throws Exception;

    /**
     * Pay invoice
     *
     * @param invoiceUuid    the invoice uuid
     * @param parkingLotUuid the parking lot uuid
     */
    void payInvoice(String invoiceUuid, String parkingLotUuid) throws Exception;
}
