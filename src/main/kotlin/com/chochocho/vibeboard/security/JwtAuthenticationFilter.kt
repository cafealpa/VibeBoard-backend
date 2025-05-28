package com.chochocho.vibeboard.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.util.StringUtils

/**
 * JWT 인증 필터
 * 모든 요청에 대해 JWT 토큰을 검증하고 인증 정보를 설정하는 필터
 */
@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    /**
     * 요청마다 JWT 토큰을 확인하고 사용자 인증을 처리하는 메소드
     * 
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 필터 체인
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val jwt = parseJwt(request)

            if (jwt != null && jwtTokenProvider.validateToken(jwt)) {
                val username = jwtTokenProvider.getUsernameFromToken(jwt)
                val userDetails = userDetailsService.loadUserByUsername(username)
                val authentication = jwtTokenProvider.getAuthentication(jwt, userDetails)

                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            logger.error("Cannot set user authentication: ${e.message}")
        }

        filterChain.doFilter(request, response)
    }

    /**
     * HTTP 요청에서 JWT 토큰을 추출하는 메소드
     * 
     * @param request HTTP 요청
     * @return 추출된 JWT 토큰 또는 null
     */
    private fun parseJwt(request: HttpServletRequest): String? {
        val headerAuth = request.getHeader("Authorization")

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7)
        }

        return null
    }
}
