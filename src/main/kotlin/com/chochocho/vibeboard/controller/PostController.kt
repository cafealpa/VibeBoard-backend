package com.chochocho.vibeboard.controller

import com.chochocho.vibeboard.dto.request.PostCreateRequest
import com.chochocho.vibeboard.dto.request.PostSearchRequest
import com.chochocho.vibeboard.dto.request.PostUpdateRequest
import com.chochocho.vibeboard.dto.response.MessageResponse
import com.chochocho.vibeboard.dto.response.PagedPostResponse
import com.chochocho.vibeboard.dto.response.PostResponse
import com.chochocho.vibeboard.service.PostService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Posts", description = "Post management API")
class PostController(
    private val postService: PostService
) {

    @Operation(
        summary = "Get all posts",
        description = "Returns a paginated list of posts with optional filtering"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved posts",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = PagedPostResponse::class))]
        )
    )
    @GetMapping
    fun getPosts(
        @Parameter(description = "Keyword to search in post title and content")
        @RequestParam(required = false) keyword: String?,

        @Parameter(description = "Page number (zero-based)")
        @RequestParam(defaultValue = "0") page: Int,

        @Parameter(description = "Number of items per page")
        @RequestParam(defaultValue = "10") size: Int,

        @Parameter(description = "Field to sort by (e.g., createdAt, title)")
        @RequestParam(defaultValue = "createdAt") sortBy: String,

        @Parameter(description = "Sort direction (asc or desc)")
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

    @Operation(
        summary = "Get post by ID",
        description = "Returns a post by its ID"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved post",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = PostResponse::class))]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Post not found",
            content = [Content(mediaType = "application/json")]
        )
    )
    @GetMapping("/{id}")
    fun getPostById(
        @Parameter(description = "ID of the post to retrieve", required = true)
        @PathVariable id: Long
    ): ResponseEntity<PostResponse> {
        val post = postService.getPostById(id)
        return ResponseEntity.ok(post)
    }

    @Operation(
        summary = "Create a new post",
        description = "Creates a new post with the provided information",
        security = [SecurityRequirement(name = "bearer-jwt")]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "201",
            description = "Post created successfully",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = PostResponse::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = [Content(mediaType = "application/json")]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(mediaType = "application/json")]
        )
    )
    @PostMapping
    fun createPost(@Valid @RequestBody postCreateRequest: PostCreateRequest): ResponseEntity<PostResponse> {
        val createdPost = postService.createPost(postCreateRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost)
    }

    @Operation(
        summary = "Update an existing post",
        description = "Updates an existing post with the provided information",
        security = [SecurityRequirement(name = "bearer-jwt")]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Post updated successfully",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = PostResponse::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = [Content(mediaType = "application/json")]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(mediaType = "application/json")]
        ),
        ApiResponse(
            responseCode = "403",
            description = "Forbidden - not the post author",
            content = [Content(mediaType = "application/json")]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Post not found",
            content = [Content(mediaType = "application/json")]
        )
    )
    @PutMapping("/{id}")
    fun updatePost(
        @Parameter(description = "ID of the post to update", required = true)
        @PathVariable id: Long,
        @Valid @RequestBody postUpdateRequest: PostUpdateRequest
    ): ResponseEntity<PostResponse> {
        val updatedPost = postService.updatePost(id, postUpdateRequest)
        return ResponseEntity.ok(updatedPost)
    }

    @Operation(
        summary = "Delete a post",
        description = "Deletes a post by its ID",
        security = [SecurityRequirement(name = "bearer-jwt")]
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Post deleted successfully",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = MessageResponse::class))]
        ),
        ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = [Content(mediaType = "application/json")]
        ),
        ApiResponse(
            responseCode = "403",
            description = "Forbidden - not the post author",
            content = [Content(mediaType = "application/json")]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Post not found",
            content = [Content(mediaType = "application/json")]
        )
    )
    @DeleteMapping("/{id}")
    fun deletePost(
        @Parameter(description = "ID of the post to delete", required = true)
        @PathVariable id: Long
    ): ResponseEntity<MessageResponse> {
        postService.deletePost(id)
        return ResponseEntity.ok(MessageResponse("Post deleted successfully"))
    }
}
