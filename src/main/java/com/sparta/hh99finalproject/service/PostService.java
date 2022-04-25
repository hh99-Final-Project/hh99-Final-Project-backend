package com.sparta.hh99finalproject.service;

import com.sparta.hh99finalproject.domain.Post;
import com.sparta.hh99finalproject.dto.request.PostCreateRequestDto;
import com.sparta.hh99finalproject.repository.PostRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 게시글(다이어리) 저장
    // toDO: 로그인한 유저만 게시글 저장 가능
    public void create(PostCreateRequestDto postCreateRequestDto) {
        Post post = new Post(postCreateRequestDto);
        postRepository.save(post);
    }

    // 게시글(다이어리) 삭제
    // toDO: 게시글 작성자만 게시글을 지울 수 있다.
    public void delete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );

        postRepository.delete(post);
    }
}
