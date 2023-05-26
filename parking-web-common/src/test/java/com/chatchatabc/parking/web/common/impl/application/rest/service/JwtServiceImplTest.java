package com.chatchatabc.parking.web.common.impl.application.rest.service;

import com.auth0.jwt.interfaces.Payload;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImplTest {
    private static final String SECRET = "your-jwt-hmac512-secret";
    private static final long EXPIRATION = 43200000L;
    private static JwtServiceImpl jwtService;

    @BeforeAll
    public static void setUp() {
        jwtService = new JwtServiceImpl(SECRET, EXPIRATION);
    }

    @Test
    public void testGenerateJwtToken() {
        final String token = jwtService.generateToken("dfc3cd78-9c89-4da2-8749-253afed080af", "member", List.of("ROLE_USER"));
        System.out.println(token);
        jwtService.validateTokenAndGetPayload(token);
    }

    @Test
    void testGenerateToken_shouldReturnValidTokenWithCorrectPayload() {
        String memberId = "1";
        String username = "testuser";
        String role1 = "ROLE_ADMIN";
        String role2 = "ROLE_USER";
        String expectedToken = jwtService.generateToken(memberId, username, Arrays.asList(role1, role2));

        assertNotNull(expectedToken);

        Payload payload = jwtService.validateTokenAndGetPayload(expectedToken);
        assertNotNull(payload);

        assertEquals(memberId, payload.getSubject(), "Token subject should match memberId");
        assertEquals("DavaoParking", payload.getIssuer(), "Token issuer should match 'DavaoParking'");
        assertEquals(username, payload.getClaim("username").asString(), "Token username claim should match");
        assertEquals(Arrays.asList(role1, role2), payload.getClaim("role").asList(String.class), "Token role claim should match");
    }

    @Test
    void testValidateTokenAndGetPayload_shouldReturnNullForInvalidToken() {
        String invalidToken = "invalid-token";
        Payload payload = jwtService.validateTokenAndGetPayload(invalidToken);
        assertNull(payload);
    }
}
