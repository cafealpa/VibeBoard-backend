package com.chochocho.vibeboard.repository

import com.chochocho.vibeboard.domain.entity.Post
import com.chochocho.vibeboard.domain.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    // Find posts by author with pagination
    fun findByAuthor(author: User, pageable: Pageable): Page<Post>
    
    // Find posts by title containing the given string with pagination
    fun findByTitleContainingIgnoreCase(title: String, pageable: Pageable): Page<Post>
    
    // Find posts by content containing the given string with pagination
    fun findByContentContainingIgnoreCase(content: String, pageable: Pageable): Page<Post>
    
    // Find posts by title or content containing the given string with pagination
    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    fun findByTitleOrContentContainingIgnoreCase(keyword: String, pageable: Pageable): Page<Post>
    
    // Find post by id and increment view count
    @Query("SELECT p FROM Post p WHERE p.id = :id")
    fun findByIdForView(id: Long): Optional<Post>
}