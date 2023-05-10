package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.Member;
import com.chatchatabc.parking.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;

class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void softRegisterMember_NewMember_SuccessfullyRegistered() throws Exception {
        // Arrange
        String phone = "1234567890";
        String username = "john_doe";
        when(memberRepository.findByPhone(phone)).thenReturn(Optional.empty());

        // Act
        memberService.softRegisterMember(phone, username);

        // Assert
        verify(memberRepository, times(1)).findByPhone(phone);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void softRegisterMember_ExistingMemberWithCorrectUsername_NoExceptionThrown() throws Exception {
        // Arrange
        String phone = "1234567890";
        String username = "john_doe";
        Member existingMember = new Member();
        existingMember.setUsername(username);
        when(memberRepository.findByPhone(phone)).thenReturn(Optional.of(existingMember));

        // Act
        memberService.softRegisterMember(phone, username);

        // Assert
        verify(memberRepository, times(1)).findByPhone(phone);
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void softRegisterMember_ExistingMemberWithIncorrectUsername_ExceptionThrown() {
        // Arrange
        String phone = "1234567890";
        String existingUsername = "john_doe";
        String incorrectUsername = "jane_smith";
        Member existingMember = new Member();
        existingMember.setUsername(existingUsername);
        when(memberRepository.findByPhone(phone)).thenReturn(Optional.of(existingMember));

        // Act & Assert
        assertThrows(Exception.class, () ->
                memberService.softRegisterMember(phone, incorrectUsername));

        verify(memberRepository, times(1)).findByPhone(phone);
        verify(memberRepository, never()).save(any(Member.class));
    }

}
