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
    void generateToken_shouldCreateToken() {
        String memberId = "memberId";
        String token = jwtService.generateToken(memberId);
        assertNotNull(token);
    }

    @Test
    void validateTokenAndGetMember_shouldReturnMember_whenValidToken() {
        String memberId = "memberId";
        Member member = new Member();
        member.setMemberUuid(memberId);
        String token = jwtService.generateToken(memberId);
        when(memberRepository.findByMemberUuid(memberId)).thenReturn(Optional.of(member));
        Member result = jwtService.validateTokenAndGetMember(token);
        assertNotNull(result);
        assertEquals(member, result);
        verify(memberRepository, times(1)).findByMemberUuid(memberId);
    }

    @Test
    void validateTokenAndGetMember_shouldReturnNull_whenInvalidToken() {
        String token = "invalidToken";
        Member result = jwtService.validateTokenAndGetMember(token);
        assertNull(result);
    }
}