package com.chatchatabc.parking.application.dto.user;

import com.chatchatabc.parking.application.dto.ErrorContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPhoneLoginResponse implements Serializable {
    private ErrorContent error;
}

