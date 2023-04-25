package com.chatchatabc.parking.application.dto.user;

import com.chatchatabc.parking.application.dto.ErrorContent;
import com.chatchatabc.parking.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable {
    private User user;
    private ErrorContent error;
}
