package com.sparta.hh99finalproject.service;

import com.sparta.hh99finalproject.domain.Post;
import com.sparta.hh99finalproject.domain.User;
import com.sparta.hh99finalproject.dto.CommentDto;
import com.sparta.hh99finalproject.dto.request.PostCreateRequestDto;
import com.sparta.hh99finalproject.dto.response.PostMyPageResponseDto;
import com.sparta.hh99finalproject.dto.response.PostOtherOnePostResponseDto;
import com.sparta.hh99finalproject.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import com.sparta.hh99finalproject.domain.Comment;
import com.sparta.hh99finalproject.dto.CommentListDto;
import com.sparta.hh99finalproject.dto.response.PostResponseDto;
import com.sparta.hh99finalproject.repository.CommentRepository;
import com.sparta.hh99finalproject.security.UserDetailsImpl;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    // 내 게시글 한 페이지당 보여줄 게시글의 수
    private static final int MY_POST_PAGEABLE_SIZE = 5;
    // 페이지 sort 대상 (id를 기준으로 내림차순으로 sort할 에정임)
    private static final String SORT_PROPERTIES = "id";
    // 남의 게시글 한 페이지당 보여줄 게시글의 수 (한 페이지당 보여줄 게시글의 수는 1개이지만 5개를 한번에 보내주기로 함)
    private static final int OTHER_POST_PAGEABLE_SIZE = 1;

    //게시글 1개 상세 조회
    public PostResponseDto getPost(Long postId, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 게시글이 존재하지 않습니다.")
        );
        // 댓글의 삭제 가능 여부를 확인한 뒤 Dto로 변환
        List<CommentDto> newCommentList = getCommentDtos(userDetails, post);

        //Dto에 담아주기
        CommentListDto commentListDto = new CommentListDto(newCommentList);

        return new PostResponseDto(post, commentListDto);
    }

    // 게시글(다이어리) 저장
    public void create(PostCreateRequestDto postCreateRequestDto, User user) {
        Post post = new Post(postCreateRequestDto, user);
        postRepository.save(post);
    }

    // 게시글(다이어리) 삭제

    public void delete(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );

        // toDO: 게시글 작성자만 게시글을 지울 수 있다.
        if (!user.getId().equals(post.getUser().getId())) {
            throw new IllegalArgumentException("게시글 작성자 만이 게시글을 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    // 나의 게시글 리스트 조회
    public List<PostMyPageResponseDto> findOneMyPage(Integer pageId, User user) {

        // paging 처리 해야 하는 수 보다 게시글의 수가 적을 경우 고려
        int postSize = Math.min(postRepository.findByUser(user).size(), MY_POST_PAGEABLE_SIZE);
        Pageable pageable = PageRequest.of(pageId, postSize, Sort.by((Direction.DESC), SORT_PROPERTIES));

        Page<Post> pagedPosts = postRepository.findByUser(user, pageable);
        List<PostMyPageResponseDto> postDtos = new ArrayList<>();

        for (Post pagedPost : pagedPosts) {
            postDtos.add(new PostMyPageResponseDto(pagedPost));
        }

        return postDtos;
    }

    public List<PostOtherOnePostResponseDto> findOneOtherPage(User user) {

        long otherPostsCount = postRepository.countByUserNot(user);
        if (otherPostsCount >= 1) {
            return getRandomOtherPosts(user, otherPostsCount);
        }

        return new ArrayList<>();
    }

    private List<PostOtherOnePostResponseDto> getRandomOtherPosts(User user, long otherPostsCount) {
        List<PostOtherOnePostResponseDto> postDtos = new ArrayList<>();
        for (int i = 0; i < Math.min(OTHER_POST_PAGEABLE_SIZE, otherPostsCount); i++) {
            int idx = (int)(Math.random() * otherPostsCount);
            Pageable pageable = PageRequest.of(idx, 1);
            Page<Post> posts = postRepository
                .findAllByUserNot(
                    user,
                    pageable
                );
            postDtos.add(new PostOtherOnePostResponseDto(posts.getContent().get(0)));
        }
        return postDtos;
    }

    //댓글에 로그인한 유저와 비교해 삭제 가능 여부 판단해주는 메소드
    private List<CommentDto> getCommentDtos(UserDetailsImpl userDetails, Post post) {
        User user = post.getUser();

        List<CommentDto> newCommentList = new ArrayList<>();
        List<Comment> commentList = commentRepository.findAllByPost(post);
        for (Comment comment: commentList) {
            if (comment.getUser().getId().equals(userDetails.getUser().getId()) || userDetails.getUser().getId().equals(user.getId())) {
                comment.setShow(true);
            }
            CommentDto commentDto = new CommentDto(comment);
            newCommentList.add(commentDto);
        }
        return newCommentList;
    }
}
