package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.specification.UserSpecification
import com.chatchatabc.parking.user
import com.chatchatabc.parking.web.common.toPagedResponse
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class UserGQLController(
    private val userRepository: UserRepository,
) {

    /**
     * Get user by any identifier
     */
    @QueryMapping
    fun getUser(@Argument id: String) = run { id.user }

    /**
     * Get all users w/ keyword
     */
    @QueryMapping
    fun getUsers(
        @Argument page: Int,
        @Argument size: Int,
        @Argument verified: Int? = null,
        @Argument keyword: String?,
        @Argument sortField: String? = null,
        @Argument sortBy: Int? = null,
    ) = run {
        val pr = PageRequest.of(page, size)
        var spec = UserSpecification.withKeyword(keyword ?: "")

        // Filter by verified
        // 0: not verified
        if (verified == 0) {
            spec = spec.and(UserSpecification.notVerified())
        }
        // 1: verified
        else if (verified == 1) {
            spec = spec.and(UserSpecification.verified())
        }

        // Sort
        if (sortField != null && sortBy != null) {
            spec = spec.and(UserSpecification.sortBy(sortField, sortBy))
        }
        userRepository.findAll(spec, pr).toPagedResponse()
    }
}