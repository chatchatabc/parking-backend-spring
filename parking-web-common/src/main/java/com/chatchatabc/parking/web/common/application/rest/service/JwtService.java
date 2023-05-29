package com.chatchatabc.parking.web.common.application.rest.service;

import com.auth0.jwt.interfaces.Payload;

import java.util.List;

public interface JwtService {

    /**
     * Generate a JWT token for the given user id
     *
     * @param userId   the user id
     * @param username the username
     * @param roles    the roles
     * @return the JWT token
     */
    String generateToken(String userId, String username, List<String> roles);

    /**
     * Validate the given token and return the jwt payload
     *
     * @param token the token
     * @return the jwt payload
     */
    Payload validateTokenAndGetPayload(String token);
}
