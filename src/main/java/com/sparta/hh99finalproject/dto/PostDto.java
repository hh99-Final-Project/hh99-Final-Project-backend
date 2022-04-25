package com.sparta.hh99finalproject.dto;

import com.sparta.hh99finalproject.domain.Post;
import com.sparta.hh99finalproject.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    private Long id;
    private String nickname;
    private String title;
    private String content;
    private boolean isShow;
    private User user;


    public PostDto(Post post1) {
        this.id = post1.getId();
        this.nickname = post1.getUser().getNickname();
        this.title = post1.getTitle();
        this.content = post1.getContent();
        this.isShow = post1.isShow();
        this.user = post1.getUser();
    }
}
