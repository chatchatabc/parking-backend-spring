package com.chatchatabc.parking.web.common.impl.application.rest.service;

import com.chatchatabc.parking.domain.model.Member;
import com.chatchatabc.parking.domain.repository.MemberRepository;
import com.chatchatabc.parking.web.common.application.rest.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceImplTest {

    private final String secret = "test-secret";
    private final String expiration = "3600000"; // 1 hour in milliseconds

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private JwtService jwtService = new JwtServiceImpl(secret, expiration);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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