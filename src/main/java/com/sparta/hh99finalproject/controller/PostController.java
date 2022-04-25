package com.sparta.hh99finalproject.controller;

import com.sparta.hh99finalproject.domain.Post;
import com.sparta.hh99finalproject.domain.User;
import com.sparta.hh99finalproject.dto.request.PostCreateRequestDto;
import com.sparta.hh99finalproject.dto.response.PostOtherOnePostResponseDto;
import com.sparta.hh99finalproject.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

    // 나의 게시글 1개 조회 (페이지당 한건, id를 기준으로 내림차순으로 반환)
    @GetMapping("/api/posts/{page}")
    public Page<Post> getMyPost(@PathVariable Integer pageId) {
        pageId -= 1;
        return postService.findOneMyPage(pageId);
    }

    // 남의 랜덤 게시글 1개 조회
    @GetMapping("/api/posts")
    public List<PostOtherOnePostResponseDto> getOtherPost() {
        User user = new User();
        return postService.findOneOtherPage(user);
    }
}
