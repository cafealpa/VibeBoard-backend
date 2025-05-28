package com.chochocho.vibeboard.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

// DTO for user registration
data class SignupRequest(
    @field:NotBlank(message = "Username is required")
    @field:Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @field:Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Username can only contain letters, numbers and underscores")
    val username: String,
    
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email should be valid")
    @field:Size(max = 100, message = "Email cannot exceed 100 characters")
    val email: String,
    
    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    val password: String,
    
    @field:Size(max = 100, message = "Full name cannot exceed 100 characters")
    val fullName: String? = null
)

// DTO for user login
data class LoginRequest(
    @field:NotBlank(message = "Username is required")
    val username: String,
    
    @field:NotBlank(message = "Password is required")
    val password: String
)

// DTO for password update
data class PasswordUpdateRequest(
    @field:NotBlank(message = "Current password is required")
    val currentPassword: String,
    
    @field:NotBlank(message = "New password is required")
    @field:Size(min = 6, max = 100, message = "New password must be between 6 and 100 characters")
    val newPassword: String
)

// DTO for profile update
data class ProfileUpdateRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email should be valid")
    @field:Size(max = 100, message = "Email cannot exceed 100 characters")
    val email: String,
    
    @field:Size(max = 100, message = "Full name cannot exceed 100 characters")
    val fullName: String? = null
)