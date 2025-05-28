package com.chochocho.vibeboard.controller

import com.chochocho.vibeboard.dto.request.PasswordUpdateRequest
import com.chochocho.vibeboard.dto.request.ProfileUpdateRequest
import com.chochocho.vibeboard.dto.response.MessageResponse
import com.chochocho.vibeboard.dto.response.UserResponse
import com.chochocho.vibeboard.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management API")
class UserController(
    private val userService: UserService
) {

    @Operation(
        summary = "Get current user profile",
        description = "Returns the profile of the currently authenticated user",
        security = [SecurityRequirement(name = "bearer-jwt")]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved user profile",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = UserResponse::class))]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(mediaType = "application/json")]
        )
    )
    @GetMapping("/me")
    fun getCurrentUser(): ResponseEntity<UserResponse> {
        val userResponse = userService.getCurrentUser()
        return ResponseEntity.ok(userResponse)
    }

    @Operation(
        summary = "Update user profile",
        description = "Updates the profile of the currently authenticated user",
        security = [SecurityRequirement(name = "bearer-jwt")]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Profile updated successfully",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = UserResponse::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = [Content(mediaType = "application/json")]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(mediaType = "application/json")]
        )
    )
    @PutMapping("/me")
    fun updateProfile(
        @Valid @RequestBody profileUpdateRequest: ProfileUpdateRequest
    ): ResponseEntity<UserResponse> {
        val updatedUser = userService.updateProfile(profileUpdateRequest)
        return ResponseEntity.ok(updatedUser)
    }

    @Operation(
        summary = "Update user password",
        description = "Updates the password of the currently authenticated user",
        security = [SecurityRequirement(name = "bearer-jwt")]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Password updated successfully",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = MessageResponse::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid input or current password is incorrect",
            content = [Content(mediaType = "application/json")]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(mediaType = "application/json")]
        )
    )
    @PutMapping("/me/password")
    fun updatePassword(
        @Valid @RequestBody passwordUpdateRequest: PasswordUpdateRequest
    ): ResponseEntity<MessageResponse> {
        val response = userService.updatePassword(passwordUpdateRequest)
        return ResponseEntity.ok(response)
    }
}
