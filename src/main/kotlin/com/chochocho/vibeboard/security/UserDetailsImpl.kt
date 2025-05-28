package com.chochocho.vibeboard.security

import com.chochocho.vibeboard.domain.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(
    private val id: Long,
    private val username: String,
    private val email: String,
    private val password: String,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    fun getId(): Long = id

    fun getEmail(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    companion object {
        fun build(user: User): UserDetailsImpl {
            val authorities = listOf(SimpleGrantedAuthority(user.role.name))

            return UserDetailsImpl(
                id = user.id!!,
                username = user.username,
                email = user.email,
                password = user.password,
                authorities = authorities
            )
        }
    }
}