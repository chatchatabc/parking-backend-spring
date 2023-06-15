package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.web.common.application.config.security.filter.JwtRequestFilter
import com.chatchatabc.parking.web.common.application.rest.service.JwtService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import javax.sql.DataSource

@ExtendWith(SpringExtension::class)
open class UnauthorizedBaseTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var jwtService: JwtService

    @MockBean
    lateinit var jwtRequestFilter: JwtRequestFilter

    @MockBean
    lateinit var dataSource: DataSource

    val objectMapper: ObjectMapper = ObjectMapper()

    /**
     * MVC GET request builder
     */
    fun get(urlTemplate: String, vararg uriVariables: Any?): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders.get(urlTemplate, *uriVariables)
    }

    /**
     * MVC POST request builder
     */
    fun post(urlTemplate: String, vararg uriVariables: Any?): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders.post(urlTemplate, *uriVariables)
    }
}