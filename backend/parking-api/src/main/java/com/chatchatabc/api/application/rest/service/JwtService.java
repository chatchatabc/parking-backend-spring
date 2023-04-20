package com.chatchatabc.api.application.rest.service;

import com.chatchatabc.api.application.dto.user.UserDTO;

public interface JwtService {

    /**
     * Generate a JWT token for the given user id
     *
     * @param user the user dto
     * @return the JWT token
     */
    String generateToken(UserDTO user);

    /**
     * Validate the given token and return the user id
     *
     * @param token the token
     * @return the user id
     */
    UserDTO validateTokenAndGetUser(String token);
}
