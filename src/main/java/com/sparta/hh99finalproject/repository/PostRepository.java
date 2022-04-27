package com.sparta.hh99finalproject.repository;

import com.sparta.hh99finalproject.domain.Post;
import com.sparta.hh99finalproject.domain.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Long countByUserNot(User user);

    List<Post> findByUser(User user);

    Page<Post> findAllByUserNot(User user, Pageable pageable);

    Page<Post> findByUser(User user, Pageable pageable);
}
