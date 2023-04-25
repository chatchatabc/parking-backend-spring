package com.chatchatabc.api.impl.application.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.chatchatabc.api.application.rest.service.JwtService;
import com.chatchatabc.parking.domain.model.User;
import com.chatchatabc.parking.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${server.jwt.secret}")
    private String secret;
    @Value("${server.jwt.expiration}")
    private String expiration;

    @Autowired
    private UserRepository userRepository;

    private final Algorithm hmac512;
    private final JWTVerifier verifier;

    public JwtServiceImpl(
            @Value("${server.jwt.secret}")
            String secret,
            @Value("${server.jwt.expiration}")
            String expiration
    ) {
        this.secret = secret;
        this.expiration = expiration;
        hmac512 = Algorithm.HMAC512(secret);
        verifier = JWT.require(hmac512).build();
    }

    /**
     * Generate a JWT token for the given user id
     *
     * @param userId the user id
     * @return the JWT token
     */
    @Override
    public String generateToken(String userId) {
        return JWT.create()
                .withSubject(userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + Long.parseLong(expiration)))
                .sign(hmac512);
    }

    /**
     * Validate the given token and return the user
     *
     * @param token the token
     * @return the user
     */
    @Override
    public User validateTokenAndGetUser(String token) {
        try {
            String userId = verifier.verify(token).getSubject();
            return userRepository.findById(userId).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}
