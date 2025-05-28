package com.chochocho.vibeboard.controller

import com.chochocho.vibeboard.dto.request.PasswordUpdateRequest
import com.chochocho.vibeboard.dto.request.ProfileUpdateRequest
import com.chochocho.vibeboard.dto.response.MessageResponse
import com.chochocho.vibeboard.dto.response.UserResponse
import com.chochocho.vibeboard.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/me")
    fun getCurrentUser(): ResponseEntity<UserResponse> {
        val userResponse = userService.getCurrentUser()
        return ResponseEntity.ok(userResponse)
    }

    @PutMapping("/me")
    fun updateProfile(
        @Valid @RequestBody profileUpdateRequest: ProfileUpdateRequest
    ): ResponseEntity<UserResponse> {
        val updatedUser = userService.updateProfile(profileUpdateRequest)
        return ResponseEntity.ok(updatedUser)
    }

    @PutMapping("/me/password")
    fun updatePassword(
        @Valid @RequestBody passwordUpdateRequest: PasswordUpdateRequest
    ): ResponseEntity<MessageResponse> {
        val response = userService.updatePassword(passwordUpdateRequest)
        return ResponseEntity.ok(response)
    }
}