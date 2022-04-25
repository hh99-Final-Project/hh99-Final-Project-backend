package com.sparta.hh99finalproject.repository;

import com.sparta.hh99finalproject.domain.Post;
import com.sparta.hh99finalproject.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Long countByUserNot(User user);

    Page<Post> findAllByUserNot(User user, PageRequest of);

    User findUserById(Long postId);
}
