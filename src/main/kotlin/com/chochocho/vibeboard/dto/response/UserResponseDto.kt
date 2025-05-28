package com.chochocho.vibeboard.dto.response

import com.chochocho.vibeboard.domain.entity.User
import java.time.LocalDateTime

// DTO for user information
data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val fullName: String?,
    val role: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun fromEntity(user: User): UserResponse {
            return UserResponse(
                id = user.id!!,
                username = user.username,
                email = user.email,
                fullName = user.fullName,
                role = user.role.name,
                createdAt = user.createdAt
            )
        }
    }
}

// DTO for authentication response
data class AuthResponse(
    val token: String,
    val type: String = "Bearer",
    val id: Long,
    val username: String,
    val email: String,
    val role: String
) {
    companion object {
        fun fromUserAndToken(user: User, token: String): AuthResponse {
            return AuthResponse(
                token = token,
                id = user.id!!,
                username = user.username,
                email = user.email,
                role = user.role.name
            )
        }
    }
}

// Simple message response
data class MessageResponse(
    val message: String
)