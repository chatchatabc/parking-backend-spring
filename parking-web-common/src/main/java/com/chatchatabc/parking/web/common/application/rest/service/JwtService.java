package com.chatchatabc.parking.web.common.application.rest.service;

import com.chatchatabc.parking.domain.model.Member;

public interface JwtService {

    /**
     * Generate a JWT token for the given member id
     *
     * @param memberId the member id
     * @return the JWT token
     */
    String generateToken(String memberId);

    /**
     * Validate the given token and return the member
     *
     * @param token the token
     * @return the member
     */
    Member validateTokenAndGetMember(String token);
}
