package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.model.Invoice;
import com.chatchatabc.parking.domain.model.Rate;

import java.math.BigDecimal;

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
     * Create invoice for vehicle manually
     *
     * @param parkingLotUuid                  the parking lot uuid
     * @param plateNumber                     the plate number
     * @param estimatedParkingDurationInHours the estimated parking duration in hours
     * @return the invoice
     * @throws Exception the exception
     */
    Invoice createInvoiceManual(String parkingLotUuid, String plateNumber, Integer estimatedParkingDurationInHours) throws Exception;

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

    /**
     * Calculate invoice
     *
     * @param invoice the invoice
     * @param rate    the rate
     * @return the big decimal
     * @throws Exception the exception
     */
    BigDecimal calculateInvoice(Invoice invoice, Rate rate) throws Exception;
}
