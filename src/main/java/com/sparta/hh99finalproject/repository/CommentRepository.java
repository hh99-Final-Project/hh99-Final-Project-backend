package com.sparta.hh99finalproject.repository;

import com.sparta.hh99finalproject.domain.Comment;
import com.sparta.hh99finalproject.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
}
