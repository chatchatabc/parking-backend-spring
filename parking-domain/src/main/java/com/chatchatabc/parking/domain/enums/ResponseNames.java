package com.chatchatabc.parking.domain.enums;

public enum ResponseNames {
    /**
     * Misc Positive
     */
    SUCCESS,
    SUCCESS_CREATE,
    SUCCESS_UPDATE,

    /**
     * User Related
     */
    USER_LOGIN_SUCCESS,
    USER_BAD_CREDENTIALS,
    USER_VERIFY_OTP_SUCCESS,

    /**
     * Invoice Related
     */
    INVOICE_END_SUCCESS,
    INVOICE_PAY_SUCCESS,
    INVOICE_VEHICLE_NOT_PARKED_TODAY,

    /**
     * Misc Negative
     */
    ERROR,
    ERROR_CREATE,
    ERROR_UPDATE,
    ERROR_NOT_FOUND,
    UNKNOWN_ERROR,
}
