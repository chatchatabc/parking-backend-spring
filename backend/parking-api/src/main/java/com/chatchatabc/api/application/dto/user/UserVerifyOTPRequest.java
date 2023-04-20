package com.chatchatabc.api.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVerifyOTPRequest implements Serializable {
    private String phone;
    private String otp;
}
