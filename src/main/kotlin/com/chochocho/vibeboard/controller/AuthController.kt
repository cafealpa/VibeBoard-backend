package com.chochocho.vibeboard.controller

import com.chochocho.vibeboard.dto.request.LoginRequest
import com.chochocho.vibeboard.dto.request.SignupRequest
import com.chochocho.vibeboard.dto.response.AuthResponse
import com.chochocho.vibeboard.dto.response.MessageResponse
import com.chochocho.vibeboard.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService
) {

    @PostMapping("/signup")
    fun registerUser(@Valid @RequestBody signupRequest: SignupRequest): ResponseEntity<MessageResponse> {
        val response = userService.registerUser(signupRequest)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/login")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<AuthResponse> {
        val response = userService.authenticateUser(loginRequest)
        return ResponseEntity.ok(response)
    }
}