package com.sparta.hh99finalproject.controller;

import com.sparta.hh99finalproject.domain.Post;
import com.sparta.hh99finalproject.domain.User;
import com.sparta.hh99finalproject.dto.request.PostCreateRequestDto;
import com.sparta.hh99finalproject.dto.response.PostResponseDto;
import com.sparta.hh99finalproject.dto.response.PostOtherOnePostResponseDto;
import com.sparta.hh99finalproject.security.UserDetailsImpl;
import com.sparta.hh99finalproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 저장
    @PostMapping("/api/posts")
    public void create(@RequestBody PostCreateRequestDto postCreateRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            new IllegalArgumentException("로그인한 사용자만 게시글을 저장할 수 있습니다.");
        }

        postService.create(postCreateRequestDto, userDetails.getUser());
    }

    //게시글 1개 상세 조회
    @GetMapping("/api/posts/{postId}")
    public PostResponseDto getPost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println("게시글 1개 상세 조회 시작");
        return postService.getPost(postId, userDetails);
    }

    // 게시글 삭제
    @PostMapping("/api/posts/{postId}")
    public void delete(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.delete(postId, userDetails.getUser());
    }


    // 나의 게시글 1개 조회 (페이지당 한건, id를 기준으로 내림차순으로 반환)
//    @GetMapping("/api/posts/{page}")
//    public Page<Post> getMyPost(@PathVariable Integer pageId) {
//        pageId -= 1;
//        return postService.findOneMyPage(pageId);
//    }

    // 남의 랜덤 게시글 1개 조회
    @GetMapping("/api/posts")
    public List<PostOtherOnePostResponseDto> getOtherPost(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return postService.findOneOtherPage(userDetails.getUser());
    }
}
