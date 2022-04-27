package com.sparta.hh99finalproject.repository;

import com.sparta.hh99finalproject.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    Optional<ChatRoom> findByRoomHashCode(int roomUsers);

}
