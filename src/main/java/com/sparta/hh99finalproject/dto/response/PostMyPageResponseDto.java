package com.sparta.hh99finalproject.dto.response;

import com.sparta.hh99finalproject.domain.Post;
import java.time.LocalDateTime;

public class PostMyPageResponseDto {
    private Long postId;
    private String nickname;
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public PostMyPageResponseDto(Post pagedPost) {
        this.postId = pagedPost.getId();
        this.nickname = pagedPost.getUser().getNickname();
        this.userId = pagedPost.getUser().getId();
        this.title = pagedPost.getTitle();
        this.content = pagedPost.getTitle();
        this.createdAt = pagedPost.getCreatedAt();
    }
}
