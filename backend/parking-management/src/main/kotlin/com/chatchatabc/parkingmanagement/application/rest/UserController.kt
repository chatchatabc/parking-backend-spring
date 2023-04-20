package com.chatchatabc.parkingmanagement.application.rest

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController {
//    /**
//     * Update user
//     */
//    @PutMapping("/update")
//    fun updateUser(
//        @RequestBody request: com.chatchatabc.service.application.dto.UserProfileUpdateRequest
//    ): ResponseEntity<com.chatchatabc.service.application.dto.UserResponse> {
//        return try {
//            // Get principal from security context
//            val principal = SecurityContextHolder.getContext().authentication.principal as User
//            val user = userService.updateUser(
//                principal.id,
//                request.username,
//                request.email,
//                request.firstName,
//                request.lastName
//            )
//            ResponseEntity.ok().body(com.chatchatabc.service.application.dto.UserResponse(user, null))
//        } catch (e: Exception) {
//            e.printStackTrace()
//            ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(
//                    com.chatchatabc.service.application.dto.UserResponse(
//                        null,
//                        ErrorContent("User Profile Update Error", e.message ?: "Unknown Error")
//                    )
//                )
//        }
//    }
}