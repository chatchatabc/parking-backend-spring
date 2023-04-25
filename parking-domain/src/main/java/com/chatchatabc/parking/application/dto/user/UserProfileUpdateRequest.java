package com.chatchatabc.parking.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpdateRequest implements Serializable {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
