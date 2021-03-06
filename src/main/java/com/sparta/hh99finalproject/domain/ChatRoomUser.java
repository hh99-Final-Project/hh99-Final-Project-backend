package com.sparta.hh99finalproject.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.sparta.hh99finalproject.domain.User;

@Getter
@Entity
@NoArgsConstructor
public class ChatRoomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 채팅방 주인
    @ManyToOne
    private User user;

    // 채팅방 이름
    private String name;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;


    public ChatRoomUser(User user, User anotherUser, ChatRoom room) {

        this.user = user;
        this.name = anotherUser.getNickname();
        this.chatRoom = room;
    }


}
