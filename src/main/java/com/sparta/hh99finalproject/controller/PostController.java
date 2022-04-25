package com.sparta.hh99finalproject.controller;

import com.sparta.hh99finalproject.dto.request.PostCreateRequestDto;
import com.sparta.hh99finalproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 저장
    @PostMapping("/api/posts")
    public void create(@RequestBody PostCreateRequestDto postCreateRequestDto) {
        postService.create(postCreateRequestDto);
    }

    // 게시글 삭제
    @PostMapping("/api/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }

}
