package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.MemberLoginRequest
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Member
import com.chatchatabc.parking.domain.model.Role
import com.chatchatabc.parking.domain.repository.MemberRepository
import com.chatchatabc.parking.domain.service.log.MemberLoginLogService
import com.chatchatabc.parking.web.common.application.rest.service.JwtService
import org.junit.Assert.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.context.ActiveProfiles
import java.util.*


@ActiveProfiles("test")
@SpringBootTest
class AuthControllerTest {
    @Mock
    private lateinit var memberRepository: MemberRepository

    @Mock
    private lateinit var authenticationManager: AuthenticationManager

    @Mock
    private lateinit var jwtService: JwtService

    @Mock
    private lateinit var memberLoginLogService: MemberLoginLogService

    @InjectMocks
    private lateinit var authController: AuthController

    @Test
    fun testLoginMember_WithValidCredentials_ShouldLoginSuccessfully() {
        // Given
        val username = "username"
        val password = "password"
        val adminRole = Role().apply { this.id = 1L; this.name = "ROLE_ADMIN" }
        val roles = listOf(adminRole)
        val memberLoginRequest = MemberLoginRequest(username, password)
        val request = MockHttpServletRequest()
        val mockMember = Member().apply {
            this.id = 1L
            this.memberUuid = "memberUuid"
            this.notificationUuid = "notificationUuid"
            this.username = username
            this.password = password
            this.roles = roles
        }
        val roleStrings: List<String> = mockMember.roles.stream()
            .map { it.authority }
            .toList()

        val usernamePasswordAuthToken =
            UsernamePasswordAuthenticationToken(memberLoginRequest.username, memberLoginRequest.password)
        Mockito.`when`(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember))
        Mockito.`when`(authenticationManager.authenticate(usernamePasswordAuthToken)).thenReturn(null)
        Mockito.`when`(jwtService.generateToken(mockMember.memberUuid, mockMember.username, roleStrings))
            .thenReturn("testToken")

        // When
        val responseEntity = authController.loginMember(memberLoginRequest, request)
        println(responseEntity)

        // Then
        Assertions.assertEquals(HttpStatus.OK, responseEntity.statusCode)
        Assertions.assertNotNull(responseEntity.headers["X-Access-Token"])
        Assertions.assertEquals("testToken", responseEntity.headers["X-Access-Token"]?.get(0))
        Assertions.assertEquals(username, (responseEntity.body?.data as Member).username)
        Assertions.assertEquals(ResponseNames.MEMBER_LOGIN_SUCCESS.name, responseEntity.body?.message)
        Assertions.assertFalse(responseEntity.body?.error!!)
        verify(memberLoginLogService, times(1)).createLog(anyLong(), anyString(), anyInt(), anyBoolean())
    }

    @Test
    fun testLoginMember_WithInvalidCredentials_ShouldReturnError() {
        // Given
        val username = "username"
        val password = "password"
        val memberLoginRequest = MemberLoginRequest(username, password)
        val request = MockHttpServletRequest()
        val mockMember = Member().apply {
            this.id = 1L
            this.memberUuid = "memberUuid"
            this.notificationUuid = "notificationUuid"
            this.username = username
            this.password = password
        }

        Mockito.`when`(memberRepository.findByUsername(username)).thenReturn(Optional.of(mockMember))
        Mockito.`when`(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java)))
            .thenThrow(RuntimeException("Invalid credentials"))

        // When
        val responseEntity = authController.loginMember(memberLoginRequest, request)

        // Then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.statusCode)
        Assertions.assertEquals(ResponseNames.MEMBER_BAD_CREDENTIALS.name, responseEntity.body?.message)
        Assertions.assertTrue(responseEntity.body?.error!!)
        verify(memberLoginLogService, times(1)).createLog(anyLong(), anyString(), anyInt(), anyBoolean())
    }
}