package com.sparta.hh99finalproject.repository;

import com.sparta.hh99finalproject.domain.ChatMessage;
import com.sparta.hh99finalproject.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    List<ChatMessage> findAllByChatRoomOrderByCreatedAt(ChatRoom chatRoom);
}
