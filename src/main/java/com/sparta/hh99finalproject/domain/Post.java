package com.sparta.hh99finalproject.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.sparta.hh99finalproject.dto.request.PostCreateRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시글 제목
    private String title;

    // 게시글 내용
    private String content;

    private boolean isShow;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    private User user;

    public Post(PostCreateRequestDto postCreateRequestDto, User user) {
        this.title = postCreateRequestDto.getTitle();
        this.content = postCreateRequestDto.getContent();
        this.user = user;
    }
}
