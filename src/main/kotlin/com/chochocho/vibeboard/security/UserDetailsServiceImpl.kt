package com.chochocho.vibeboard.security

import com.chochocho.vibeboard.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 사용자 상세 정보 서비스 구현 클래스
 * Spring Security의 UserDetailsService 인터페이스를 구현하여 사용자 인증 정보를 로드
 */
@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {

    /**
     * 사용자 이름으로 사용자 정보를 로드하는 메소드
     * 
     * @param username 사용자 이름
     * @return 사용자 상세 정보
     * @throws UsernameNotFoundException 사용자를 찾을 수 없을 때 발생
     */
    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("User not found with username: $username") }

        return UserDetailsImpl.build(user)
    }
}
