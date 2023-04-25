package com.chatchatabc.parking.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPhoneLoginRequest implements Serializable {
    private String phone;
    private String username;
}
