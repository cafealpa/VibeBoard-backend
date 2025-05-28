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

@Configuration
class DataInitializer {

    @Bean
    @Profile("!prod") // Only run in non-production environments
    fun initData(
        userRepository: UserRepository,
        postRepository: PostRepository,
        passwordEncoder: PasswordEncoder
    ): CommandLineRunner {
        return CommandLineRunner {
            // Create admin user
            val admin = User(
                username = "admin",
                email = "admin@example.com",
                password = passwordEncoder.encode("password"),
                fullName = "Admin User",
                role = User.Role.ROLE_ADMIN
            )
            userRepository.save(admin)
            
            // Create regular user
            val user = User(
                username = "user",
                email = "user@example.com",
                password = passwordEncoder.encode("password"),
                fullName = "Regular User"
            )
            userRepository.save(user)
            
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
        }
    }
}