package com.chochocho.vibeboard.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

// DTO for creating a post
data class PostCreateRequest(
    @field:NotBlank(message = "Title is required")
    @field:Size(min = 2, max = 200, message = "Title must be between 2 and 200 characters")
    val title: String,
    
    @field:NotBlank(message = "Content is required")
    @field:Size(min = 10, message = "Content must be at least 10 characters")
    val content: String
)

// DTO for updating a post
data class PostUpdateRequest(
    @field:NotBlank(message = "Title is required")
    @field:Size(min = 2, max = 200, message = "Title must be between 2 and 200 characters")
    val title: String,
    
    @field:NotBlank(message = "Content is required")
    @field:Size(min = 10, message = "Content must be at least 10 characters")
    val content: String
)

// DTO for searching posts
data class PostSearchRequest(
    val keyword: String? = null,
    val page: Int = 0,
    val size: Int = 10,
    val sortBy: String = "createdAt",
    val sortDirection: String = "desc"
)