package com.chatchatabc.api.application.dto.user;

import com.chatchatabc.api.application.dto.role.RoleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
    private String id;
    private String email;
    private String username;
    private String phone;
    private String firstName;
    private String lastName;
    private Set<RoleDTO> roles;

    // TODO: Add the other dates?
}
