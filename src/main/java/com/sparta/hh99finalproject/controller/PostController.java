package com.sparta.hh99finalproject.controller;

import com.sparta.hh99finalproject.dto.request.PostCreateRequestDto;
import com.sparta.hh99finalproject.dto.response.PostMyPageResponseDto;
import com.sparta.hh99finalproject.dto.response.PostOtherOnePostResponseDto;
import com.sparta.hh99finalproject.dto.response.PostResponseDto;
import com.sparta.hh99finalproject.security.UserDetailsImpl;
import com.sparta.hh99finalproject.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

    // 나의 게시글 1개 조회 (페이지당 한건, id를 기준으로 내림차순으로 반환)
    @GetMapping("/api/myposts/{pageId}")
    public List<PostMyPageResponseDto> getMyPost(@PathVariable Integer pageId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        pageId -= 1;
        return postService.findOneMyPage(pageId, userDetails.getUser());
    }

    // 남의 랜덤 게시글 5개 조회
    @GetMapping("/api/posts")
    public List<PostOtherOnePostResponseDto> getOtherPost(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return postService.findOneOtherPage(userDetails.getUser());
    }

    //게시글 1개 상세 조회
    @GetMapping("/api/posts/{postId}")
    public PostResponseDto getPost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.getPost(postId, userDetails);
    }

    // 게시글 삭제
    @DeleteMapping("/api/posts/{postId}")
    public void delete(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.delete(postId, userDetails.getUser());
    }
}
