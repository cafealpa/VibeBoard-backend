package com.chochocho.vibeboard.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true, length = 50)
    var username: String,

    @Column(nullable = false, unique = true, length = 100)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Column(name = "full_name", length = 100)
    var fullName: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role = Role.ROLE_USER,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    enum class Role {
        ROLE_USER, ROLE_ADMIN
    }
    
    // Methods to update user information
    fun updateProfile(fullName: String?, email: String) {
        this.fullName = fullName
        this.email = email
        this.updatedAt = LocalDateTime.now()
    }
    
    fun updatePassword(newPassword: String) {
        this.password = newPassword
        this.updatedAt = LocalDateTime.now()
    }
}