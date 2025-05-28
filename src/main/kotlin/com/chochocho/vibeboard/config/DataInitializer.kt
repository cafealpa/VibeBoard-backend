package com.chochocho.vibeboard.config

import com.chochocho.vibeboard.domain.entity.Post
import com.chochocho.vibeboard.domain.entity.User
import com.chochocho.vibeboard.repository.PostRepository
import com.chochocho.vibeboard.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * 데이터 초기화를 담당하는 설정 클래스
 * 개발 환경에서 테스트 데이터를 생성합니다.
 */
@Configuration
class DataInitializer {

    /**
     * 애플리케이션 시작 시 초기 데이터를 생성하는 메소드
     * 
     * @param userRepository 사용자 저장소
     * @param postRepository 게시물 저장소
     * @param passwordEncoder 비밀번호 인코더
     * @return 애플리케이션 시작 시 실행될 CommandLineRunner
     */
    @Bean
    @Profile("!prod") // Only run in non-production environments
    fun initData(
        userRepository: UserRepository,
        postRepository: PostRepository,
        passwordEncoder: PasswordEncoder
    ): CommandLineRunner {
        return CommandLineRunner {
            // Check if data already exists
            if (userRepository.count() > 0 && postRepository.count() > 0) {
                println("Data already exists, skipping initialization.")
                return@CommandLineRunner
            }

            // Create admin user if it doesn't exist
            var admin: User
            if (!userRepository.existsByUsername("admin")) {
                admin = User(
                    username = "admin",
                    email = "admin@example.com",
                    password = passwordEncoder.encode("password"),
                    fullName = "Admin User",
                    role = User.Role.ROLE_ADMIN
                )
                userRepository.save(admin)
            } else {
                admin = userRepository.findByUsername("admin").get()
            }

            // Create regular user if it doesn't exist
            var user: User
            if (!userRepository.existsByUsername("user")) {
                user = User(
                    username = "user",
                    email = "user@example.com",
                    password = passwordEncoder.encode("password"),
                    fullName = "Regular User"
                )
                userRepository.save(user)
            } else {
                user = userRepository.findByUsername("user").get()
            }

            // Create posts only if there are no posts
            if (postRepository.count() == 0L) {
                // Create some posts
                val post1 = Post(
                    title = "Welcome to VibeBoard",
                    content = "This is the first post on our bulletin board system. Feel free to explore and create your own posts!",
                    author = admin
                )
                postRepository.save(post1)

                val post2 = Post(
                    title = "Getting Started with Kotlin",
                    content = """
                        Kotlin is a modern programming language that makes developers happier.
                        It's concise, safe, interoperable with Java, and provides many ways to reuse code between multiple platforms.

                        Here are some key features:
                        - Null safety
                        - Extension functions
                        - Data classes
                        - Coroutines
                    """.trimIndent(),
                    author = admin
                )
                postRepository.save(post2)

                val post3 = Post(
                    title = "My First Post",
                    content = "Hello everyone! This is my first post on VibeBoard. I'm excited to be part of this community!",
                    author = user
                )
                postRepository.save(post3)

                println("Data initialization completed!")
            } else {
                println("Posts already exist, skipping post creation.")
            }
        }
    }
}
