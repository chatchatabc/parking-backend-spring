package com.chatchatabc.parking.application.rest.service;

import com.chatchatabc.parking.domain.model.User;

public interface JwtService {

    /**
     * Generate a JWT token for the given user id
     *
     * @param userId the user id
     * @return the JWT token
     */
    String generateToken(String userId);

    /**
     * Validate the given token and return the user
     *
     * @param token the token
     * @return the user
     */
    User validateTokenAndGetUser(String token);
}
