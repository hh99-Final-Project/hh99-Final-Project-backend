package com.sparta.hh99finalproject.dto.response;

import com.sparta.hh99finalproject.dto.request.CommentRequestDto;
import com.sparta.hh99finalproject.security.UserDetailsImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {

    private Long postId;
    private String nickname;
    private String content;
    private String createdAt;
    private boolean isShow;

    public CommentResponseDto(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails, String dateResult) {
        this.postId = commentRequestDto.getPostId();
        this.nickname = userDetails.getUser().getNickname();
        this.content = commentRequestDto.getContent();
        this.createdAt = dateResult;
        this.isShow = true;
    }
}
