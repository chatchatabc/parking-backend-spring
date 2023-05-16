package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.model.Invoice;

public interface InvoiceService {

    /**
     * Create invoice for vehicle
     *
     * @param parkingLotUuid the parking lot uuid
     * @param vehicleId      the vehicle id
     * @return the invoice
     */
    Invoice createInvoice(String parkingLotUuid, String vehicleId) throws Exception;

    /**
     * End invoice
     *
     * @param invoiceId      the invoice id
     * @param parkingLotUuid the parking lot uuid
     * @return the invoice
     */
    Invoice endInvoice(String invoiceId, String parkingLotUuid) throws Exception;

    /**
     * Pay invoice
     *
     * @param invoiceId      the invoice id
     * @param parkingLotUuid the parking lot uuid
     * @return the invoice
     */
    Invoice payInvoice(String invoiceId, String parkingLotUuid) throws Exception;
}
