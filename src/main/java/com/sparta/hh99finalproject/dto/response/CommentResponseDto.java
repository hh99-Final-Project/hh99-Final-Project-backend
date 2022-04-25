package com.sparta.hh99finalproject.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {

    private Long postId;
    private String nickname;
    private String content;
    private String createdAt;
    private boolean isShow;
}
