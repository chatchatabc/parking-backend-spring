package com.chatchatabc.parking.web.common.impl.application.rest.service;

import com.auth0.jwt.interfaces.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class JwtServiceImplTest {
    private static final String SECRET = "my-secret";
    private static final long EXPIRATION = 60000L;
    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl(SECRET, EXPIRATION);
    }

    @Test
    void generateToken_shouldReturnValidToken() {
        String memberId = "1";
        String username = "testuser";
        String role1 = "admin";
        String role2 = "user";
        String expectedToken = jwtService.generateToken(memberId, username, Arrays.asList(role1, role2));
        assertNotNull(expectedToken);
        Payload payload = jwtService.validateTokenAndGetPayload(expectedToken);
        assertEquals(memberId, payload.getSubject());
        assertEquals("DavaoParking", payload.getIssuer());
        assertEquals(username, payload.getClaim("username").asString());
        assertEquals(Arrays.asList(role1, role2), payload.getClaim("role").asList(String.class));
    }

    @Test
    void validateTokenAndGetPayload_shouldReturnNullForInvalidToken() {
        String invalidToken = "invalid-token";
        Payload payload = jwtService.validateTokenAndGetPayload(invalidToken);
        assertNull(payload);
    }

    @Test
    void validateTokenAndGetMember_shouldReturnNull_whenInvalidToken() {
        String token = "invalidToken";
        Member result = jwtService.validateTokenAndGetMember(token);
        assertNull(result);
    }
}