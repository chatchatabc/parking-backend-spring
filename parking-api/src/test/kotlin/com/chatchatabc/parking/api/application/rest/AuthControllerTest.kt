package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.event.user.UserLoginEvent
import com.chatchatabc.parking.domain.model.Role
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.service.UserService
import com.chatchatabc.parking.domain.service.log.UserLoginLogService
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [AuthController::class])
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest : UnauthorizedBaseTest() {
    @MockBean
    lateinit var userService: UserService

    @MockBean
    lateinit var userLoginLogService: UserLoginLogService

    @MockBean
    lateinit var applicationEventPublisher: ApplicationEventPublisher

    @Test
    fun testLogin_ShouldBeSuccessful() {
        // Given
        val phone = "+1234567890"
        val username = "admin"
        val userPhoneLoginRequest = AuthController.UserPhoneLoginRequest(phone, username)
        val otp = "000000"

        // Behaviors
        BDDMockito.willDoNothing().given(userService).softRegisterUser(phone, username)
        BDDMockito.given(userService.generateOTPAndSaveToKV(phone, 900L)).willReturn(otp)
        BDDMockito.willDoNothing().given(applicationEventPublisher).publishEvent(UserLoginEvent(this, phone, otp))

        // Actions
        this.mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userPhoneLoginRequest))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
    }

    @Test
    fun testLogin_ShouldBeFailed() {
        // Given
        val phone = "+1234567890"
        val username = "admin"
        val userPhoneLoginRequest = AuthController.UserPhoneLoginRequest(phone, username)

        // Behaviors
        BDDMockito.given(userService.softRegisterUser(phone, username))
            .willThrow(RuntimeException("Username is incorrect"))

        // Actions
        this.mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userPhoneLoginRequest))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is4xxClientError())
    }

    @Test
    fun testVerifyOTP_ShouldBeSuccessful() {
        // Given
        val token = "token"
        val phone = "+1234567890"
        val otp = "000000"
        val roleName: Role.RoleNames = Role.RoleNames.ROLE_USER
        val userVerifyOTPRequest = AuthController.UserVerifyOTPRequest(phone, otp)
        val user = User().apply {
            this.id = 1
            this.userUuid = "1"
            this.username = "admin"
            this.phone = phone
            this.roles = listOf(Role(4, "ROLE_USER"))
        }
        val remoteAddr = "127.0.0.1"
        val roleStrings: List<String> = user.roles?.stream()
            ?.map { it.authority }
            ?.toList() ?: emptyList()

        // Mock HttpServletRequest
        val request = mock(HttpServletRequest::class.java)
        `when`(request.remoteAddr).thenReturn(remoteAddr)

        // Behaviors
        BDDMockito.given(userService.verifyOTPAndAddRole(phone, otp, roleName)).willReturn(user)
        BDDMockito.given(jwtService.generateToken(user.userUuid, user.username, roleStrings)).willReturn(token)
        BDDMockito.willDoNothing().given(userLoginLogService).createLog(user.id, remoteAddr, 0, true)

        // Actions
        this.mockMvc.perform(
            post("/api/auth/verify/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userVerifyOTPRequest))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            // Check if the token from header "X-Access-Token" is equal to the token from jwtService
            .andExpect { result ->
                val tokenFromHeader = result.response.getHeader("X-Access-Token")
                assert(tokenFromHeader == token)
            }
    }
}