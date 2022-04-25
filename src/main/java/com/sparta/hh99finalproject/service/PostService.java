package com.sparta.hh99finalproject.service;

import com.sparta.hh99finalproject.domain.Post;
import com.sparta.hh99finalproject.domain.User;
import com.sparta.hh99finalproject.dto.request.PostCreateRequestDto;
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
    private static final int MY_POST_PAGEABLE_SIZE = 1;
    // 페이지 sort 대상 (id를 기준으로 내림차순으로 sort할 에정임)
    private static final String SORT_PROPERTIES = "id";
    // 남의 게시글 한 페이지당 보여줄 게시글의 수 (한 페이지당 보여줄 게시글의 수는 1개이지만 5개를 한번에 보내주기로 함)
    private static final int OTHER_POST_PAGEABLE_SIZE = 5;

    //게시글 1개 상세 조회
    public PostResponseDto getPost(Long postId, UserDetailsImpl userDetails) {
        Optional<Post> post = postRepository.findById(postId);
        Post post1 = post.get();

        User user = postRepository.findUserById(postId);

        List<Comment> newCommentList = new ArrayList<>();
        List<Comment> commentLists = commentRepository.findAllByPost(post1);
        for (Comment commentList : commentLists) {
            if (commentList.getUser().getId().equals(userDetails.getUser().getId()) || userDetails.getUser().getId().equals(user.getId())) {
                commentList.setShow(true);
            } else {
                commentList.setShow(false);
            }
            newCommentList.add(commentList);
        }

        CommentListDto commentListDto = new CommentListDto(newCommentList);

        return new PostResponseDto(post1, commentListDto);
    }

    // 게시글(다이어리) 저장
    public void create(PostCreateRequestDto postCreateRequestDto) {
        Post post = new Post(postCreateRequestDto);
        postRepository.save(post);
    }

    // 게시글(다이어리) 삭제
    // toDO: 게시글 작성자만 게시글을 지울 수 있다.
    public void delete(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 게시글입니다.")
        );

        if (!user.getId().equals(post.getUser().getId())) {
            throw new IllegalArgumentException("게시글 작성자 만이 게시글을 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    public Page<Post> findOneMyPage(Integer pageId) {
        Pageable pageable = PageRequest.of(pageId, MY_POST_PAGEABLE_SIZE, Sort.by((Direction.DESC), SORT_PROPERTIES));
         return postRepository.findAll(pageable);
    }

    public List<PostOtherOnePostResponseDto> findOneOtherPage(User user) {

        Long otherPostsSize = postRepository.countByUserNot(user);
        int idx = (int)(Math.random() * otherPostsSize);

        // 페이징 처리해서 남의 게시글 중 한개만 뽑아 오기
        Page<Post> postPage = postRepository
            .findAllByUserNot(
                user,
                PageRequest.of(idx, OTHER_POST_PAGEABLE_SIZE)
            );

        List<Post> posts = postPage.getContent();
        List<PostOtherOnePostResponseDto> postDtos = new ArrayList<>();
        for (Post post : posts) {
            postDtos.add(new PostOtherOnePostResponseDto(post));
        }
        return postDtos;
    }
}
