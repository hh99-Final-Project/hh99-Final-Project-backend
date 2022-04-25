package com.sparta.hh99finalproject.service;

import com.sparta.hh99finalproject.domain.Post;
import com.sparta.hh99finalproject.dto.request.PostCreateRequestDto;
import com.sparta.hh99finalproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void create(PostCreateRequestDto postCreateRequestDto) {
        Post post = new Post(postCreateRequestDto);
    }
}
