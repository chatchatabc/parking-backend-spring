package com.chatchatabc.parking.domain.service;

public interface InvoiceService {

    /**
     * Create invoice for vehicle
     *
     * @param parkingLotUuid the parking lot uuid
     * @param vehicleUuid    the vehicle uuid
     */
    void createInvoice(String parkingLotUuid, String vehicleUuid) throws Exception;

    /**
     * End invoice
     *
     * @param invoiceId      the invoice id
     * @param parkingLotUuid the parking lot uuid
     */
    void endInvoice(String invoiceId, String parkingLotUuid) throws Exception;

    /**
     * Pay invoice
     *
     * @param invoiceId      the invoice id
     * @param parkingLotUuid the parking lot uuid
     */
    void payInvoice(String invoiceId, String parkingLotUuid) throws Exception;
}
