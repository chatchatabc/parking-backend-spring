package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.web.common.impl.application.rest.service.JwtServiceImpl
import org.springframework.context.annotation.Import
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@WithMockUser(username = "member", password = "123456")
@Import(JwtServiceImpl::class)
open class AuthorizedBaseTest {

    fun get(urlTemplate: String, vararg uriVariables: Any?): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders.get(urlTemplate, *uriVariables).header("Authorization", "Bearer ${getToken()}")
    }

    fun post(urlTemplate: String, vararg uriVariables: Any?): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders.post(urlTemplate, *uriVariables).header("Authorization", "Bearer ${getToken()}")
    }

    open fun getToken(): String {
        // member uuid: "dfc3cd78-9c89-4da2-8749-253afed080af",  name: "member"
        return "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJkZmMzY2Q3OC05Yzg5LTRkYTItODc0OS0yNTNhZmVkMDgwYWYiLCJpc3MiOiJEYXZhb1BhcmtpbmciLCJ1c2VybmFtZSI6Im1lbWJlciIsInJvbGUiOlsiUk9MRV9NRU1CRVIiXSwiaWF0IjoxNjg1MDgxMjU5LCJleHAiOjE2ODUxMjQ0NTl9.O75eDzGRgdkWxL-7hVwFwAwZvLERHtBkAhZMvSYT4N2gf4KMsYdVDNyx8LrCW2MvQ5W1dPRbc_NlhopFOk7YvA"
    }
}