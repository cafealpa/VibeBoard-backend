package com.chochocho.vibeboard.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}")
    private val jwtSecret: String,

    @Value("\${jwt.expiration}")
    private val jwtExpirationMs: Long
) {
    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    }

    // Generate JWT token
    fun generateToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as UserDetailsImpl

        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationMs)

        return Jwts.builder()
            .setSubject(userPrincipal.getUsername())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    // Get username from JWT token
    fun getUsernameFromToken(token: String): String {
        val claims: Claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

        return claims.subject
    }

    // Validate JWT token
    fun validateToken(token: String): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
            return true
        } catch (e: Exception) {
            // Log the exception
            return false
        }
    }

    // Create Authentication object from JWT token
    fun getAuthentication(token: String, userDetails: UserDetails): Authentication {
        return UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.authorities
        )
    }
}
