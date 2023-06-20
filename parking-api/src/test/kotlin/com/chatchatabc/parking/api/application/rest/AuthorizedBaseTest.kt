package com.chatchatabc.parking.api.application.rest

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.chatchatabc.parking.web.common.application.config.security.filter.JwtRequestFilter
import com.chatchatabc.parking.web.common.application.rest.service.JwtService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.web.context.WebApplicationContext
import java.util.*
import javax.sql.DataSource

@ExtendWith(SpringExtension::class)
@ContextConfiguration
@WithMockUser(username = "dfc3cd78-9c89-4da2-8749-253afed080af", password = "123456", roles = ["ADMIN"])
open class AuthorizedBaseTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var context: WebApplicationContext

    @MockBean
    lateinit var jwtService: JwtService

    @MockBean
    lateinit var jwtRequestFilter: JwtRequestFilter

    @MockBean
    lateinit var dataSource: DataSource

    private val token: String by lazy { generateToken() }

    val objectMapper: ObjectMapper = ObjectMapper()

//    @BeforeEach
//    fun setup() {
//        val mockPayload = Mockito.mock(Payload::class.java)
//        val mockClaim = Mockito.mock(Claim::class.java)
//
//        Mockito.`when`(mockPayload.getSubject()).thenReturn("dfc3cd78-9c89-4da2-8749-253afed080af")
//        Mockito.`when`(mockClaim.asArray(String::class.java)).thenReturn(arrayOf("ADMIN"))
//        Mockito.`when`(mockPayload.getClaim("role")).thenReturn(mockClaim)
//        Mockito.`when`(jwtService.validateTokenAndGetPayload(ArgumentMatchers.anyString())).thenReturn(mockPayload)
//
//        // Mock JwtRequestFilter
//        val mockJwtRequestFilter = JwtRequestFilter(jwtService)
//
//        // Configure MockMvc
//        mvc = MockMvcBuilders
//            .webAppContextSetup(context)
//            .apply<DefaultMockMvcBuilder>(springSecurity())
//            .addFilter<DefaultMockMvcBuilder>(mockJwtRequestFilter, "/*")
//            .build()
//    }

    fun get(urlTemplate: String, vararg uriVariables: Any?): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders.get(urlTemplate, *uriVariables).header("Authorization", "Bearer $token")
    }

    fun post(urlTemplate: String, vararg uriVariables: Any?): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders.post(urlTemplate, *uriVariables).header("Authorization", "Bearer $token")
    }

    // Based on JwtService generateToken
    open fun generateToken(): String {
        val jwtSecret = "your-hmac512-secret"
        val hmac512 = Algorithm.HMAC512(jwtSecret)
//
//        val userDetails = SecurityContextHolder.getContext().authentication.principal as UserDetails
//        val username = userDetails.username // the uuid
        val username = "dfc3cd78-9c89-4da2-8749-253afed080af" // the uuid
        val roles = listOf("ADMIN")

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