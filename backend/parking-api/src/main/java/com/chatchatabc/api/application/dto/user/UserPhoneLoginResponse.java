package com.chatchatabc.api.application.dto.user;

import com.chatchatabc.api.application.dto.ErrorContent;
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
