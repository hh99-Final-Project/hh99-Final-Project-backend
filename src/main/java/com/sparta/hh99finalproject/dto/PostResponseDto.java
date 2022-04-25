package com.sparta.hh99finalproject.dto;

import com.sparta.hh99finalproject.domain.Comment;
import com.sparta.hh99finalproject.domain.Post;
import com.sparta.hh99finalproject.domain.User;
import com.sparta.hh99finalproject.security.UserDetailsImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostResponseDto {

    private Long postId;
    private Long userId;
    private String nickname;
    private String title;
    private String content;
    private CommentListDto comments;
    private boolean isShow;

    public PostResponseDto(PostDto postDto, CommentListDto commentList, UserDetailsImpl userDetails) {
        this.postId = postDto.getId();
        this.userId = postDto.getUser().getId();
        this.nickname = postDto.getNickname();
        this.title = postDto.getTitle();
        this.content = postDto.getContent();
        this.comments = commentList;
        this.isShow = (postDto.getUser().getId().equals(userDetails.getUser().getId()));
    }
}
