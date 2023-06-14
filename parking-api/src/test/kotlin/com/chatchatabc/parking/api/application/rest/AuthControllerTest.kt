package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.event.user.UserLoginEvent
import com.chatchatabc.parking.domain.service.UserService
import com.chatchatabc.parking.domain.service.log.UserLoginLogService
import com.chatchatabc.parking.web.common.application.config.security.filter.JwtRequestFilter
import com.chatchatabc.parking.web.common.application.rest.service.JwtService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.sql.DataSource

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [AuthController::class])
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var userService: UserService

    @MockBean
    lateinit var jwtService: JwtService

    @MockBean
    lateinit var userLoginLogService: UserLoginLogService

    @MockBean
    lateinit var applicationEventPublisher: ApplicationEventPublisher

    @MockBean
    lateinit var jwtRequestFilter: JwtRequestFilter

    @MockBean
    lateinit var dataSource: DataSource

    val objectMapper: ObjectMapper = ObjectMapper()

    @Test
    fun testLogin_ShouldBeSuccessful() {
        // Given
        val phone = "1234567890"
        val username = "admin"
        val userPhoneLoginRequest = AuthController.UserPhoneLoginRequest(phone, username)
        val otp = "000000"

        // Behaviors
        BDDMockito.willDoNothing().given(userService).softRegisterUser(phone, username)
        BDDMockito.given(userService.generateOTPAndSaveToKV(phone, 900L)).willReturn(otp)
        BDDMockito.willDoNothing().given(applicationEventPublisher).publishEvent(UserLoginEvent(this, phone, otp))

        val builder = MockMvcRequestBuilders.post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userPhoneLoginRequest))

        mockMvc.perform(builder)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
    }

    @Test
    fun testLogin_ShouldBeFailed() {
        // Given
        val phone = "1234567890"
        val username = "admin"
        val userPhoneLoginRequest = AuthController.UserPhoneLoginRequest(phone, username)
        val otp = "000000"

        // Behaviors
        BDDMockito.given(userService.softRegisterUser(phone, username))
            .willThrow(RuntimeException("Username is incorrect"))

        val builder = MockMvcRequestBuilders.post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userPhoneLoginRequest))

        mockMvc.perform(builder)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().is4xxClientError())
    }
}