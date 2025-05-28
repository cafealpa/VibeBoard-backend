package com.chochocho.vibeboard.security

import com.chochocho.vibeboard.domain.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * 사용자 인증 정보를 담는 클래스
 * Spring Security의 UserDetails 인터페이스를 구현하여 인증된 사용자 정보를 제공
 */
class UserDetailsImpl(
    private val id: Long,
    private val username: String,
    private val email: String,
    private val password: String,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {

    /**
     * 사용자의 권한 목록을 반환하는 메소드
     * 
     * @return 사용자의 권한 목록
     */
    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    /**
     * 사용자의 비밀번호를 반환하는 메소드
     * 
     * @return 사용자의 비밀번호
     */
    override fun getPassword(): String = password

    /**
     * 사용자의 이름을 반환하는 메소드
     * 
     * @return 사용자의 이름
     */
    override fun getUsername(): String = username

    /**
     * 사용자의 ID를 반환하는 메소드
     * 
     * @return 사용자의 ID
     */
    fun getId(): Long = id

    /**
     * 사용자의 이메일을 반환하는 메소드
     * 
     * @return 사용자의 이메일
     */
    fun getEmail(): String = email

    /**
     * 계정이 만료되지 않았는지 확인하는 메소드
     * 
     * @return 계정이 만료되지 않았으면 true
     */
    override fun isAccountNonExpired(): Boolean = true

    /**
     * 계정이 잠겨있지 않은지 확인하는 메소드
     * 
     * @return 계정이 잠겨있지 않으면 true
     */
    override fun isAccountNonLocked(): Boolean = true

    /**
     * 자격 증명이 만료되지 않았는지 확인하는 메소드
     * 
     * @return 자격 증명이 만료되지 않았으면 true
     */
    override fun isCredentialsNonExpired(): Boolean = true

    /**
     * 계정이 활성화되어 있는지 확인하는 메소드
     * 
     * @return 계정이 활성화되어 있으면 true
     */
    override fun isEnabled(): Boolean = true

    companion object {
        /**
         * User 엔티티로부터 UserDetailsImpl 객체를 생성하는 메소드
         * 
         * @param user 사용자 엔티티
         * @return 생성된 UserDetailsImpl 객체
         */
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
