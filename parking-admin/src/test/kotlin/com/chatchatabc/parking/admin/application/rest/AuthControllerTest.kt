package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.service.log.UserLoginLogService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@WebMvcTest(controllers = [AuthController::class])
class AuthControllerTest : AuthorizedBaseTest() {
    @MockBean
    private lateinit var userRepository: UserRepository

    @MockBean
    private lateinit var authenticationManager: AuthenticationManager

    @MockBean
    private lateinit var userLoginLogService: UserLoginLogService

    private val objectMapper = ObjectMapper()

//    @Test
//    // Ignore @WithMockUser of AuthorizedBaseTest
//    @WithAnonymousUser
//    fun testLoginUser_WithValidCredentials_ShouldLoginSuccessfully() {
//        val username = "admin"
//        val password = "123456"
//        val encryptedPassword = "\$2a\$10\$HfewouomThstiUJu.zfYPOsLJahJHCVnqS7GbEz0KFBQjiZUcsINK"
//        val userUuid = "userUuid"
//        val notificationUuid = "notificationUuid"
//        val adminRole = Role().apply { this.id = 1L; this.name = "ROLE_ADMIN" }
//        val roles = listOf(adminRole)
//        val userLoginRequest = UserLoginRequest(username, password)
//
//        val mockUser = User().apply {
//            this.id = 1L
//            this.userUuid = userUuid
//            this.notificationUuid = notificationUuid
//            this.username = username
//            this.password = encryptedPassword
//            this.roles = roles
//        }
//
//        val usernamePasswordAuthToken =
//            UsernamePasswordAuthenticationToken(userLoginRequest.username, userLoginRequest.password)
//
//        given(userRepository.findByUsername(username)).willReturn(Optional.of(mockUser))
//        given(authenticationManager.authenticate(usernamePasswordAuthToken)).willReturn(usernamePasswordAuthToken)
////        given(userLoginLogService.createLog(1L, "127.0.0.1", 0, true))
//
//        val json = objectMapper.writeValueAsString(userLoginRequest)
//
//        val result = this.mvc.perform(
//            postNoAuth("/api/auth/login")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json)
//                .characterEncoding("utf-8")
//        )
//            .andExpect(status().isOk())
//            .andExpect(header().string("X-Access-Token", notNullValue(String::class.java))).andReturn()
//    }

//    @Test
//    fun testLoginUser_WithValidCredentials_ShouldLoginSuccessfully() {
//        // Given
//        val username = "username"
//        val password = "password"
//        val adminRole = Role().apply { this.id = 1L; this.name = "ROLE_ADMIN" }
//        val roles = listOf(adminRole)
//        val userLoginRequest = UserLoginRequest(username, password)
//        val request = MockHttpServletRequest()
//        val mockUser = User().apply {
//            this.id = 1L
//            this.userUuid = "userUuid"
//            this.notificationUuid = "notificationUuid"
//            this.username = username
//            this.password = password
//            this.roles = roles
//        }
//        val roleStrings: List<String> = mockUser.roles.stream()
//            .map { it.authority }
//            .toList()
//
//        val usernamePasswordAuthToken =
//            UsernamePasswordAuthenticationToken(userLoginRequest.username, userLoginRequest.password)
//        Mockito.`when`(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser))
//        Mockito.`when`(authenticationManager.authenticate(usernamePasswordAuthToken)).thenReturn(null)
//        Mockito.`when`(jwtService.generateToken(mockUser.userUuid, mockUser.username, roleStrings))
//            .thenReturn("testToken")
//
//        // When
//        val responseEntity = authController.loginUser(userLoginRequest, request)
//        println(responseEntity)
//
//        // Then
//        Assertions.assertEquals(HttpStatus.OK, responseEntity.statusCode)
//        Assertions.assertNotNull(responseEntity.headers["X-Access-Token"])
//        Assertions.assertEquals("testToken", responseEntity.headers["X-Access-Token"]?.get(0))
//        Assertions.assertEquals(username, (responseEntity.body?.data as User).username)
//        Assertions.assertEquals(ResponseNames.USER_LOGIN_SUCCESS.name, responseEntity.body?.message)
//        Assertions.assertFalse(responseEntity.body?.error!!)
//        verify(userLoginLogService, times(1)).createLog(anyLong(), anyString(), anyInt(), anyBoolean())
//    }

//    @Test
//    fun testLoginUser_WithInvalidCredentials_ShouldReturnError() {
//        // Given
//        val username = "username"
//        val password = "password"
//        val userLoginRequest = UserLoginRequest(username, password)
//        val request = MockHttpServletRequest()
//        val mockUser = User().apply {
//            this.id = 1L
//            this.userUuid = "userUuid"
//            this.notificationUuid = "notificationUuid"
//            this.username = username
//            this.password = password
//        }
//
//        Mockito.`when`(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser))
//        Mockito.`when`(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken::class.java)))
//            .thenThrow(RuntimeException("Invalid credentials"))
//
//        // When
//        val responseEntity = authController.loginUser(userLoginRequest, request)
//
//        // Then
//        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.statusCode)
//        Assertions.assertEquals(ResponseNames.USER_BAD_CREDENTIALS.name, responseEntity.body?.message)
//        Assertions.assertTrue(responseEntity.body?.error!!)
//        verify(userLoginLogService, times(1)).createLog(anyLong(), anyString(), anyInt(), anyBoolean())
//    }
}