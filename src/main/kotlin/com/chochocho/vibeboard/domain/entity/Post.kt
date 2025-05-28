package com.chochocho.vibeboard.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "posts")
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 200)
    var title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val author: User,

    @Column(name = "view_count", nullable = false)
    var viewCount: Int = 0,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {

    constructor() : this(
        title = "",
        content = "",
        author = User()
    )

    // Method to update post
    fun update(title: String, content: String) {
        this.title = title
        this.content = content
        this.updatedAt = LocalDateTime.now()
    }
    
    // Method to increment view count
    fun incrementViewCount() {
        this.viewCount++
    }
    
    // Method to check if user is the author
    fun isAuthor(user: User): Boolean {
        return this.author.id == user.id
    }
}