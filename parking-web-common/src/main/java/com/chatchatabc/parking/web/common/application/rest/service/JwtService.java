package com.chatchatabc.parking.web.common.application.rest.service;

import com.auth0.jwt.interfaces.Payload;

import java.util.List;

public interface JwtService {

    /**
     * Generate a JWT token for the given member id
     *
     * @param memberId the member id
     * @param username the username
     * @param roles    the roles
     * @return the JWT token
     */
    String generateToken(String memberId, String username, List<String> roles);

    /**
     * Validate the given token and return the jwt payload
     *
     * @param token the token
     * @return the jwt payload
     */
    Payload validateTokenAndGetPayload(String token);
}
