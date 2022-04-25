package com.sparta.hh99finalproject.controller;

import com.sparta.hh99finalproject.dto.request.PostCreateRequestDto;
import com.sparta.hh99finalproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/api/posts")
    public void create(@RequestBody PostCreateRequestDto postCreateRequestDto) {
        postService.create(postCreateRequestDto);
    }
}
