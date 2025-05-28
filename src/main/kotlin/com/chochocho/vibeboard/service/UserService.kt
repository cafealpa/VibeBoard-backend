package com.chochocho.vibeboard.service

import com.chochocho.vibeboard.domain.entity.User
import com.chochocho.vibeboard.dto.request.LoginRequest
import com.chochocho.vibeboard.dto.request.PasswordUpdateRequest
import com.chochocho.vibeboard.dto.request.ProfileUpdateRequest
import com.chochocho.vibeboard.dto.request.SignupRequest
import com.chochocho.vibeboard.dto.response.AuthResponse
import com.chochocho.vibeboard.dto.response.MessageResponse
import com.chochocho.vibeboard.dto.response.UserResponse
import com.chochocho.vibeboard.repository.UserRepository
import com.chochocho.vibeboard.security.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Transactional
    fun registerUser(signupRequest: SignupRequest): MessageResponse {
        // Check if username is already taken
        if (userRepository.existsByUsername(signupRequest.username)) {
            throw IllegalArgumentException("Username is already taken")
        }

        // Check if email is already in use
        if (userRepository.existsByEmail(signupRequest.email)) {
            throw IllegalArgumentException("Email is already in use")
        }

        // Create new user
        val user = User(
            username = signupRequest.username,
            email = signupRequest.email,
            password = passwordEncoder.encode(signupRequest.password),
            fullName = signupRequest.fullName
        )

        userRepository.save(user)
        return MessageResponse("User registered successfully")
    }

    fun authenticateUser(loginRequest: LoginRequest): AuthResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )

        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtTokenProvider.generateToken(authentication)
        val user = userRepository.findByUsername(loginRequest.username).get()

        return AuthResponse.fromUserAndToken(user, jwt)
    }

    fun getCurrentUser(): UserResponse {
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication.name
        val user = userRepository.findByUsername(username)
            .orElseThrow { IllegalStateException("Current user not found") }

        return UserResponse.fromEntity(user)
    }

    @Transactional
    fun updateProfile(profileUpdateRequest: ProfileUpdateRequest): UserResponse {
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication.name
        val user = userRepository.findByUsername(username)
            .orElseThrow { IllegalStateException("Current user not found") }

        // Check if email is already in use by another user
        val existingUserWithEmail = userRepository.findByEmail(profileUpdateRequest.email)
        if (existingUserWithEmail.isPresent && existingUserWithEmail.get().id != user.id) {
            throw IllegalArgumentException("Email is already in use")
        }

        user.updateProfile(profileUpdateRequest.fullName, profileUpdateRequest.email)
        val updatedUser = userRepository.save(user)

        return UserResponse.fromEntity(updatedUser)
    }

    @Transactional
    fun updatePassword(passwordUpdateRequest: PasswordUpdateRequest): MessageResponse {
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication.name
        val user = userRepository.findByUsername(username)
            .orElseThrow { IllegalStateException("Current user not found") }

        // Check if current password is correct
        if (!passwordEncoder.matches(passwordUpdateRequest.currentPassword, user.password)) {
            throw IllegalArgumentException("Current password is incorrect")
        }

        user.updatePassword(passwordEncoder.encode(passwordUpdateRequest.newPassword))
        userRepository.save(user)

        return MessageResponse("Password updated successfully")
    }
}