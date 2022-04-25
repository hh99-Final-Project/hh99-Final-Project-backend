package com.sparta.hh99finalproject.repository;

import com.sparta.hh99finalproject.domain.ChatRoomUser;
import com.sparta.hh99finalproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser,Long> {
    List<ChatRoomUser> findAllByUser(User user);

    ChatRoomUser findByUser(User user);
}
