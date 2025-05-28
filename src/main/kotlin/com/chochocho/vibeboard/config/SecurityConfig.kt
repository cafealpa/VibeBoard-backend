package com.chochocho.vibeboard.config

import com.chochocho.vibeboard.security.JwtAuthenticationFilter
import com.chochocho.vibeboard.security.UserDetailsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/**
 * 애플리케이션의 보안 설정을 담당하는 클래스
 * Spring Security 설정, 인증 및 권한 부여 규칙을 정의합니다.
 *
 * @param userDetailsService 사용자 세부 정보 서비스 구현체
 * @param jwtAuthenticationFilter JWT 인증 필터
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val userDetailsService: UserDetailsServiceImpl,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

    /**
     * 인증 제공자를 구성하는 메소드
     * 사용자 세부 정보 서비스와 비밀번호 인코더를 설정합니다.
     *
     * @return 구성된 DaoAuthenticationProvider 객체
     */
    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    /**
     * 인증 관리자를 제공하는 메소드
     * 
     * @param authConfig 인증 설정 객체
     * @return 인증 관리자 객체
     */
    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager {
        return authConfig.authenticationManager
    }

    /**
     * 비밀번호 인코더를 제공하는 메소드
     * BCrypt 알고리즘을 사용하여 비밀번호를 암호화합니다.
     *
     * @return 비밀번호 인코더 객체
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /**
     * 보안 필터 체인을 구성하는 메소드
     * HTTP 요청에 대한 보안 규칙, CORS, CSRF, 세션 관리 등을 설정합니다.
     *
     * @param http HttpSecurity 객체
     * @return 구성된 SecurityFilterChain 객체
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .requestMatchers("/api/posts").permitAll()
                    .requestMatchers("/api/posts/{id}").permitAll()
                    .anyRequest().authenticated()
            }
            .headers { headers -> headers.frameOptions { it.sameOrigin() } } // For H2 console
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    /**
     * CORS 설정을 제공하는 메소드
     * Cross-Origin Resource Sharing 정책을 구성합니다.
     *
     * @return CORS 설정 소스 객체
     */
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*")
//        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedMethods = listOf("GET", "POST", "DELETE")
        configuration.allowedHeaders = listOf("Authorization", "Content-Type")
        configuration.allowCredentials = false
        configuration.maxAge = 3600L

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
