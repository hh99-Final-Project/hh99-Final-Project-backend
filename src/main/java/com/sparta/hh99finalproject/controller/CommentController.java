package com.sparta.hh99finalproject.controller;

import com.sparta.hh99finalproject.dto.request.CommentRequestDto;
import com.sparta.hh99finalproject.dto.response.CommentResponseDto;
import com.sparta.hh99finalproject.security.UserDetailsImpl;
import com.sparta.hh99finalproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //댓글 저장
    @PostMapping("/api/comments")
    public CommentResponseDto saveComment(CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.saveComment(commentRequestDto, userDetails);
    }
    //댓글 삭제
    @DeleteMapping("/api/comments/{commentId}")
    public void deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
    }
}
