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
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    //댓글 저장
    public CommentResponseDto saveComment(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {

        Post post= postRepository.findById(commentRequestDto.getPostId()).orElseThrow(
                () -> new IllegalArgumentException("해당하는 게시글이 존재하지 않습니다.")
        );
        Comment comment = new Comment(commentRequestDto, userDetails, post);
        commentRepository.save(comment);

        //댓글 작성시간
        String dateResult = getCurrentTime();

        return new CommentResponseDto(commentRequestDto, userDetails, dateResult);

    }

    //댓글 삭제
    public void deleteComment(Long commentId, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new IllegalArgumentException("해당하는 댓글이 존재하지 않습니다.")
        );
        if (!comment.getUser().equals(userDetails.getUser())){
            throw new IllegalArgumentException("글을 작성한 유저만 삭제할 수 있습니다.");
        }
        commentRepository.deleteById(commentId);
    }

    //현재시간 추출 메소드
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yy:MM:dd hh:mm:ss");
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return sdf.format(date);
    }
}
