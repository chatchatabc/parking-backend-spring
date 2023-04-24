package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.model.Invoice;

public interface InvoiceService {

    /**
     * Create invoice for vehicle
     *
     * @param parkingLotId the parking lot id
     * @param vehicleId    the vehicle id
     * @return the invoice
     */
    Invoice createInvoice(String parkingLotId, String vehicleId) throws Exception;

    /**
     * End invoice
     *
     * @param invoiceId    the invoice id
     * @param parkingLotId the parking lot id
     * @return the invoice
     */
    Invoice endInvoice(String invoiceId, String parkingLotId) throws Exception;

    /**
     * Pay invoice
     *
     * @param invoiceId    the invoice id
     * @param parkingLotId the parking lot id
     * @return the invoice
     */
    Invoice payInvoice(String invoiceId, String parkingLotId) throws Exception;
}
