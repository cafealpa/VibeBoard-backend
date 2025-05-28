package com.chochocho.vibeboard.service

import com.chochocho.vibeboard.domain.entity.Post
import com.chochocho.vibeboard.dto.request.PostCreateRequest
import com.chochocho.vibeboard.dto.request.PostSearchRequest
import com.chochocho.vibeboard.dto.request.PostUpdateRequest
import com.chochocho.vibeboard.dto.response.PagedPostResponse
import com.chochocho.vibeboard.dto.response.PostResponse
import com.chochocho.vibeboard.repository.PostRepository
import com.chochocho.vibeboard.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {

    @Transactional(readOnly = true)
    fun getPosts(searchRequest: PostSearchRequest): PagedPostResponse {
        val direction = if (searchRequest.sortDirection.equals("asc", ignoreCase = true)) {
            Sort.Direction.ASC
        } else {
            Sort.Direction.DESC
        }
        
        val pageable = PageRequest.of(
            searchRequest.page,
            searchRequest.size,
            Sort.by(direction, searchRequest.sortBy)
        )
        
        val page = if (searchRequest.keyword != null && searchRequest.keyword.isNotBlank()) {
            postRepository.findByTitleOrContentContainingIgnoreCase(searchRequest.keyword, pageable)
        } else {
            postRepository.findAll(pageable)
        }
        
        return PagedPostResponse.fromPage(page)
    }
    
    @Transactional(readOnly = true)
    fun getPostById(id: Long): PostResponse {
        val post = postRepository.findById(id)
            .orElseThrow { NoSuchElementException("Post not found with id: $id") }
        
        // Increment view count
        post.incrementViewCount()
        postRepository.save(post)
        
        return PostResponse.fromEntity(post)
    }
    
    @Transactional
    fun createPost(postCreateRequest: PostCreateRequest): PostResponse {
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication.name
        
        val user = userRepository.findByUsername(username)
            .orElseThrow { IllegalStateException("Current user not found") }
        
        val post = Post(
            title = postCreateRequest.title,
            content = postCreateRequest.content,
            author = user
        )
        
        val savedPost = postRepository.save(post)
        return PostResponse.fromEntity(savedPost)
    }
    
    @Transactional
    fun updatePost(id: Long, postUpdateRequest: PostUpdateRequest): PostResponse {
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication.name
        
        val user = userRepository.findByUsername(username)
            .orElseThrow { IllegalStateException("Current user not found") }
        
        val post = postRepository.findById(id)
            .orElseThrow { NoSuchElementException("Post not found with id: $id") }
        
        // Check if the current user is the author of the post
        if (!post.isAuthor(user)) {
            throw AccessDeniedException("You are not authorized to update this post")
        }
        
        post.update(postUpdateRequest.title, postUpdateRequest.content)
        val updatedPost = postRepository.save(post)
        
        return PostResponse.fromEntity(updatedPost)
    }
    
    @Transactional
    fun deletePost(id: Long) {
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication.name
        
        val user = userRepository.findByUsername(username)
            .orElseThrow { IllegalStateException("Current user not found") }
        
        val post = postRepository.findById(id)
            .orElseThrow { NoSuchElementException("Post not found with id: $id") }
        
        // Check if the current user is the author of the post
        if (!post.isAuthor(user)) {
            throw AccessDeniedException("You are not authorized to delete this post")
        }
        
        postRepository.delete(post)
    }
}