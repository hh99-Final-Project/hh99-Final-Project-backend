package com.sparta.hh99finalproject.service;

import com.sparta.hh99finalproject.domain.Comment;
import com.sparta.hh99finalproject.domain.Post;
import com.sparta.hh99finalproject.domain.User;
import com.sparta.hh99finalproject.dto.CommentListDto;
import com.sparta.hh99finalproject.dto.PostDto;
import com.sparta.hh99finalproject.dto.PostResponseDto;
import com.sparta.hh99finalproject.repository.CommentRepository;
import com.sparta.hh99finalproject.repository.PostRepository;
import com.sparta.hh99finalproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    //게시글 1개 상세 조회
    public PostResponseDto getPost(Long postId, UserDetailsImpl userDetails) {
        Optional<Post> post = postRepository.findById(postId);
        Post post1 = post.get();

        User user = postRepository.findUserById(postId);

        List<Comment> newCommentList = new ArrayList<>();
        List<Comment> commentLists = commentRepository.findAllByPost(post1);
        for(Comment commentList : commentLists){
            if(commentList.getUser().getId().equals(userDetails.getUser().getId()) || userDetails.getUser().getId().equals(user.getId())){
                commentList.setShow(true);
            }else{
                commentList.setShow(false);
            }
            newCommentList.add(commentList);
        }

        PostDto postDto = new PostDto(post1);
        CommentListDto commentListDto = new CommentListDto(newCommentList);

        return new PostResponseDto(postDto, commentListDto, userDetails);
    }
}
