package com.chochocho.vibeboard.dto.response

import com.chochocho.vibeboard.domain.entity.Post
import java.time.LocalDateTime
import org.springframework.data.domain.Page

// DTO for post details
data class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val authorId: Long,
    val authorUsername: String,
    val viewCount: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromEntity(post: Post): PostResponse {
            return PostResponse(
                id = post.id!!,
                title = post.title,
                content = post.content,
                authorId = post.author.id!!,
                authorUsername = post.author.username,
                viewCount = post.viewCount,
                createdAt = post.createdAt,
                updatedAt = post.updatedAt
            )
        }
    }
}

// DTO for post summary (used in listings)
data class PostSummaryResponse(
    val id: Long,
    val title: String,
    val authorUsername: String,
    val viewCount: Int,
    val createdAt: LocalDateTime
) {
    companion object {
        fun fromEntity(post: Post): PostSummaryResponse {
            return PostSummaryResponse(
                id = post.id!!,
                title = post.title,
                authorUsername = post.author.username,
                viewCount = post.viewCount,
                createdAt = post.createdAt
            )
        }
    }
}

// DTO for paginated post response
data class PagedPostResponse(
    val content: List<PostSummaryResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val last: Boolean
) {
    companion object {
        fun fromPage(page: Page<Post>): PagedPostResponse {
            return PagedPostResponse(
                content = page.content.map { PostSummaryResponse.fromEntity(it) },
                page = page.number,
                size = page.size,
                totalElements = page.totalElements,
                totalPages = page.totalPages,
                last = page.isLast
            )
        }
    }
}