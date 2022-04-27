package com.sparta.hh99finalproject.service;

import com.sparta.hh99finalproject.domain.Comment;
import com.sparta.hh99finalproject.domain.Post;
import com.sparta.hh99finalproject.dto.request.CommentRequestDto;
import com.sparta.hh99finalproject.dto.response.CommentResponseDto;
import com.sparta.hh99finalproject.repository.CommentRepository;
import com.sparta.hh99finalproject.repository.PostRepository;
import com.sparta.hh99finalproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    //댓글 저장
    public CommentResponseDto saveComment(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {

        Optional<Post> post= postRepository.findById(commentRequestDto.getPostId());
        Post post1 = post.get();
        Comment comment = new Comment(commentRequestDto, userDetails, post1);
        commentRepository.save(comment);

        //댓글 작성시간
        SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd hh:mm:ss");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String dateResult = sdf.format(date);

        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setPostId(commentRequestDto.getPostId());
        commentResponseDto.setContent(commentRequestDto.getContent());
        commentResponseDto.setNickname(userDetails.getUser().getNickname());
        commentResponseDto.setCreatedAt(dateResult);
        commentResponseDto.setShow(true);

        return commentResponseDto;
    }
    //댓글 삭제
    public void deleteComment(Long commentId, UserDetailsImpl userDetails) {
        if(!commentRepository.findById(commentId).get().getUser().equals(userDetails.getUser())) throw new IllegalArgumentException("글을 작성한 유저만 삭제할 수 있습니다.");
        commentRepository.deleteById(commentId);
    }
}
