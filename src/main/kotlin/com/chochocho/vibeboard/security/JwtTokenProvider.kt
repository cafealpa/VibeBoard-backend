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

/**
 * JWT 토큰 제공자
 * JWT 토큰 생성, 검증 및 사용자 정보 추출을 담당하는 클래스
 */
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

    /**
     * 인증 객체로부터 JWT 토큰을 생성하는 메소드
     * 
     * @param authentication 인증 객체
     * @return 생성된 JWT 토큰
     */
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

    /**
     * JWT 토큰에서 사용자 이름을 추출하는 메소드
     * 
     * @param token JWT 토큰
     * @return 토큰에서 추출한 사용자 이름
     */
    fun getUsernameFromToken(token: String): String {
        val claims: Claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

        return claims.subject
    }

    /**
     * JWT 토큰의 유효성을 검증하는 메소드
     * 
     * @param token 검증할 JWT 토큰
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
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

    /**
     * JWT 토큰과 사용자 정보로부터 인증 객체를 생성하는 메소드
     * 
     * @param token JWT 토큰
     * @param userDetails 사용자 상세 정보
     * @return 생성된 인증 객체
     */
    fun getAuthentication(token: String, userDetails: UserDetails): Authentication {
        return UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.authorities
        )
    }
}
