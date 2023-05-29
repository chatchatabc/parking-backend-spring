package com.chatchatabc.parking.web.common.impl.application.rest.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Payload;
import com.chatchatabc.parking.web.common.application.rest.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JwtServiceImpl implements JwtService {
    private final Long expiration;


    private final Algorithm hmac512;
    private final JWTVerifier verifier;

    public JwtServiceImpl(
            @Value("${server.jwt.secret}")
            String secret,
            @Value("${server.jwt.expiration}")
            Long expiration
    ) {
        this.expiration = expiration;
        hmac512 = Algorithm.HMAC512(secret);
        verifier = JWT.require(hmac512).build();
    }

    /**
     * Generate a JWT token for the given user id
     *
     * @param userId   the user id
     * @param username the username
     * @param roles    the roles
     * @return the JWT token
     */
    @Override
    public String generateToken(String userId, String username, List<String> roles) {
        return JWT.create()
                .withSubject(userId)
                .withIssuer("DavaoParking")
                .withClaim("username", username)
                .withClaim("role", roles)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(hmac512);
    }

    /**
     * Validate the given token and return the jwt payload
     *
     * @param token the token
     * @return the jwt payload
     */
    @Override
    public Payload validateTokenAndGetPayload(String token) {
        try {
            return verifier.verify(token);
        } catch (Exception e) {
            return null;
        }
    }
}
