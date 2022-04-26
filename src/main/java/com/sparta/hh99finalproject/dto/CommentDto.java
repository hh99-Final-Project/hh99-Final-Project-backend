package com.sparta.hh99finalproject.dto;

import com.sparta.hh99finalproject.domain.Comment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {

    private Long id;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private boolean isShow;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.isShow = comment.isShow();
    }
}
