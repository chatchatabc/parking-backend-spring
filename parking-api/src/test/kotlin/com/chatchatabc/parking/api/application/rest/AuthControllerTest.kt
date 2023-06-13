package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.UserPhoneLoginRequest
import com.chatchatabc.parking.domain.service.UserService
import com.chatchatabc.parking.domain.service.log.UserLoginLogService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [AuthController::class])
class AuthControllerTest : AuthorizedBaseTest() {
    @MockBean
    lateinit var userService: UserService

    @MockBean
    lateinit var applicationEventPublisher: ApplicationEventPublisher

    @MockBean
    lateinit var userLoginLogService: UserLoginLogService

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun testLogin_ShouldBeSuccessful() {
        // Given
        val phone = "09123456789"
        val username = "admin"
        val userPhoneLoginRequest = UserPhoneLoginRequest(phone, username)
        val otp = "123456"

        // Behavior
        BDDMockito.willDoNothing().given(userService).softRegisterUser(phone, username)
        BDDMockito.given(userService.generateOTPAndSaveToKV(phone, 900L)).willReturn(otp)
        BDDMockito.willDoNothing().given(applicationEventPublisher).publishEvent(otp)

        // Perform
        val result = this.mvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userPhoneLoginRequest))
        )
            .andExpect(status().isOk())
    }
}