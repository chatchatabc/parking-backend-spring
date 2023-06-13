package com.chatchatabc.parking.api.application.rest

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.chatchatabc.parking.web.common.application.config.security.SecurityConfig
import com.chatchatabc.parking.web.common.application.config.security.filter.JwtRequestFilter
import com.chatchatabc.parking.web.common.application.rest.service.JwtService
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.util.*
import javax.sql.DataSource

@WebAppConfiguration
@ContextConfiguration(classes = [SecurityConfig::class])
@WithMockUser(username = "dfc3cd78-9c89-4da2-8749-253afed080af", password = "123456", roles = ["ADMIN"])
open class AuthorizedBaseTest {
    @Autowired
    lateinit var mvc: MockMvc

    @Autowired
    lateinit var context: WebApplicationContext

    @MockBean
    lateinit var jwtService: JwtService

    @MockBean
    lateinit var jwtRequestFilter: JwtRequestFilter

    @MockBean
    lateinit var dataSource: DataSource

    private val token: String by lazy { generateToken() }

    @BeforeEach
    fun setup() {
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
            .build()
    }

    fun get(urlTemplate: String, vararg uriVariables: Any?): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders.get(urlTemplate, *uriVariables).header("Authorization", "Bearer $token")
    }

    // TODO: integrate as one with get()
    fun getNoAuth(urlTemplate: String, vararg uriVariables: Any?): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders.get(urlTemplate, *uriVariables)
    }

    fun post(urlTemplate: String, vararg uriVariables: Any?): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders.post(urlTemplate, *uriVariables).header("Authorization", "Bearer $token")
    }

    // TODO: integrate as one with post()
    fun postNoAuth(urlTemplate: String, vararg uriVariables: Any?): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders.post(urlTemplate, *uriVariables)
    }

    // Based on JwtService generateToken
    open fun generateToken(): String {
        val jwtSecret = "your-hmac512-secret"
        val hmac512 = Algorithm.HMAC512(jwtSecret)

        val userDetails = SecurityContextHolder.getContext().authentication.principal as UserDetails
        val username = userDetails.username // the uuid
        val roles = userDetails.authorities.map { it.authority }

        return JWT.create()
            .withSubject(username)
            .withIssuer("DavaoParking")
            .withClaim("username", username)
            .withClaim("role", roles)
            .withIssuedAt(Date(System.currentTimeMillis()))
            .withExpiresAt(Date(System.currentTimeMillis() + 43200000))
            .sign(hmac512)
    }
}