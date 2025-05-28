package com.chochocho.vibeboard.controller

import com.chochocho.vibeboard.dto.request.PostCreateRequest
import com.chochocho.vibeboard.dto.request.PostSearchRequest
import com.chochocho.vibeboard.dto.request.PostUpdateRequest
import com.chochocho.vibeboard.dto.response.MessageResponse
import com.chochocho.vibeboard.dto.response.PagedPostResponse
import com.chochocho.vibeboard.dto.response.PostResponse
import com.chochocho.vibeboard.service.PostService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/posts")
class PostController(
    private val postService: PostService
) {

    @GetMapping
    fun getPosts(
        @RequestParam(required = false) keyword: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "createdAt") sortBy: String,
        @RequestParam(defaultValue = "desc") sortDirection: String
    ): ResponseEntity<PagedPostResponse> {
        val searchRequest = PostSearchRequest(
            keyword = keyword,
            page = page,
            size = size,
            sortBy = sortBy,
            sortDirection = sortDirection
        )
        val response = postService.getPosts(searchRequest)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getPostById(@PathVariable id: Long): ResponseEntity<PostResponse> {
        val post = postService.getPostById(id)
        return ResponseEntity.ok(post)
    }

    @PostMapping
    fun createPost(@Valid @RequestBody postCreateRequest: PostCreateRequest): ResponseEntity<PostResponse> {
        val createdPost = postService.createPost(postCreateRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost)
    }

    @PutMapping("/{id}")
    fun updatePost(
        @PathVariable id: Long,
        @Valid @RequestBody postUpdateRequest: PostUpdateRequest
    ): ResponseEntity<PostResponse> {
        val updatedPost = postService.updatePost(id, postUpdateRequest)
        return ResponseEntity.ok(updatedPost)
    }

    @DeleteMapping("/{id}")
    fun deletePost(@PathVariable id: Long): ResponseEntity<MessageResponse> {
        postService.deletePost(id)
        return ResponseEntity.ok(MessageResponse("Post deleted successfully"))
    }
}