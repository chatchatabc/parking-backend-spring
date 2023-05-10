package com.chatchatabc.parking.domain.enums;

public enum ResponseNames {
    /**
     * Misc Positive
     */
    SUCCESS,
    SUCCESS_CREATE,
    SUCCESS_UPDATE,

    /**
     * Member Related
     */
    MEMBER_LOGIN_SUCCESS,
    MEMBER_BAD_CREDENTIALS,
    MEMBER_VERIFY_OTP_SUCCESS,

    /**
     * Invoice Related
     */
    INVOICE_END_SUCCESS,
    INVOICE_PAY_SUCCESS,

    /**
     * Misc Negative
     */
    ERROR,
    ERROR_CREATE,
    ERROR_UPDATE,
    ERROR_NOT_FOUND,
    UNKNOWN_ERROR,
}
