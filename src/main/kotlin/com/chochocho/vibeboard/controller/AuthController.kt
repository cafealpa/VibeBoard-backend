package com.chochocho.vibeboard.controller

import com.chochocho.vibeboard.dto.request.LoginRequest
import com.chochocho.vibeboard.dto.request.SignupRequest
import com.chochocho.vibeboard.dto.response.AuthResponse
import com.chochocho.vibeboard.dto.response.MessageResponse
import com.chochocho.vibeboard.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication API")
class AuthController(
    private val userService: UserService
) {

    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account with the provided information"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "User registered successfully",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = MessageResponse::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid input or username/email already taken",
            content = [Content(mediaType = "application/json")]
        )
    )
    @PostMapping("/signup")
    fun registerUser(@Valid @RequestBody signupRequest: SignupRequest): ResponseEntity<MessageResponse> {
        val response = userService.registerUser(signupRequest)
        return ResponseEntity.ok(response)
    }

    @Operation(
        summary = "Authenticate user",
        description = "Authenticates a user and returns a JWT token"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Authentication successful",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = AuthResponse::class))]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Invalid username or password",
            content = [Content(mediaType = "application/json")]
        )
    )
    @PostMapping("/login")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<AuthResponse> {
        val response = userService.authenticateUser(loginRequest)
        return ResponseEntity.ok(response)
    }
}
