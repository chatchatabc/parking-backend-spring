package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.specification.UserSpecification
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class UserGQLController(
    private val userRepository: UserRepository,
) {

    /**
     * Get user by uuid
     */
    @QueryMapping
    fun getUserByUuid(
        @Argument uuid: String
    ): Optional<User> {
        return userRepository.findByUserUuid(uuid)
    }

    /**
     * Get user by username
     */
    @QueryMapping
    fun getUserByUsername(
        @Argument username: String
    ): Optional<User> {
        return userRepository.findByUsername(username)
    }

    /**
     * Get user by phone
     */
    @QueryMapping
    fun getUserByPhone(
        @Argument phone: String
    ): Optional<User> {
        return userRepository.findByPhone(phone)
    }

    /**
     * Get all users w/ keyword
     */
    @QueryMapping
    fun getUsers(
        @Argument page: Int,
        @Argument size: Int,
        @Argument keyword: String?
    ): PagedResponse<User> {
        val pr = PageRequest.of(page, size)
        val spec = UserSpecification.withKeyword(keyword ?: "")
        val users = userRepository.findAll(spec, pr)
        return PagedResponse(
            users.content,
            PageInfo(
                users.size,
                users.totalElements,
                users.isFirst,
                users.isLast,
                users.isEmpty
            )
        )
    }
}