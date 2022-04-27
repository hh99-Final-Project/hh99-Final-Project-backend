package com.sparta.hh99finalproject.service;

import com.sparta.hh99finalproject.domain.ChatMessage;
import com.sparta.hh99finalproject.domain.ChatRoom;
import com.sparta.hh99finalproject.domain.ChatRoomUser;
import com.sparta.hh99finalproject.domain.User;
import com.sparta.hh99finalproject.dto.ChatRoomResponseDto;
import com.sparta.hh99finalproject.dto.request.ChatRoomUserRequestDto;
import com.sparta.hh99finalproject.repository.ChatMessageRepository;
import com.sparta.hh99finalproject.repository.ChatRoomRepository;
import com.sparta.hh99finalproject.repository.ChatRoomUserRepository;
import com.sparta.hh99finalproject.repository.UserRepository;
import com.sparta.hh99finalproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ChatRoomService {


    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatMessageRepository chatMessageRepository;

    public void creatChatRoom(ChatRoomUserRequestDto requestDto, UserDetailsImpl userDetails) {

        //상대방 방도 생성>상대방 찾기

        User anotherUser = userRepository.findById(requestDto.getUserId()).orElseThrow(
                () -> new IllegalArgumentException("상대방이 존재하지 않습니다.")
        );
//
        //roomHashCode 만들기
        int roomHashCode = createRoomHashCode(userDetails, anotherUser);

        //방 존재 확인 함수
        existRoom(roomHashCode, userDetails, anotherUser);

        //방 먼저 생성
        ChatRoom room = new ChatRoom(roomHashCode);
        chatRoomRepository.save(room);

        //내 방
        ChatRoomUser chatRoomUser = new ChatRoomUser(userDetails.getUser(), anotherUser, room);
        //다른 사람 방
        ChatRoomUser chatRoomAnotherUser = new ChatRoomUser(anotherUser, userDetails.getUser(), room);

        //저장
        chatRoomUserRepository.save(chatRoomUser);
        chatRoomUserRepository.save(chatRoomAnotherUser);
    }

    //for 둘 다 있는 방 판단
    public int createRoomHashCode(UserDetailsImpl userDetails, User anotherUser) {
        Long userId = userDetails.getUser().getId();
        Long anotherId = anotherUser.getId();
        return userId > anotherId ? Objects.hash(userId, anotherId) : Objects.hash(anotherId, userId);
    }

    //이미 방이 존재할 때
    public void existRoom(int roomUsers, UserDetailsImpl userDetails, User anotherUser) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomHashCode(roomUsers).orElse(null);

        if (chatRoom != null) {
            List<ChatRoomUser> chatRoomUser = chatRoom.getChatRoomUsers();
            if (chatRoomUser.size() == 2) {
                throw new IllegalArgumentException("이미 존재하는 방입니다.");
            } else if (chatRoomUser.size() == 1) {
                //나만 있을 때
                if (chatRoomUser.get(0).getUser().equals(userDetails.getUser())) {
                    ChatRoomUser user = new ChatRoomUser(anotherUser, userDetails.getUser(), chatRoom);
                    chatRoomUserRepository.save(user);
                }
                //상대방만 있을 때
                else {
                    ChatRoomUser user = new ChatRoomUser(userDetails.getUser(), anotherUser, chatRoom);
                    chatRoomUserRepository.save(user);
                }
            }
        }
    }



    public List<ChatRoomResponseDto> getChatRoom(UserDetailsImpl userDetails) {
        //user로 챗룸 유저를 찾고>>챗룸 유저에서 채팅방을 찾는다
        //마자
        //마지막나온 메시지 ,내용 ,시간
        List<ChatRoomResponseDto> responseDtoList = new ArrayList<>();
        List<ChatRoomUser> chatRoomUserList = chatRoomUserRepository.findAllByUser(userDetails.getUser());


        for (ChatRoomUser chatRoomUser : chatRoomUserList) {

            ChatRoomResponseDto responseDto = createChatRoomDto(chatRoomUser);
            responseDtoList.add(responseDto);

            //정렬
            responseDtoList.sort(Collections.reverseOrder());

        }
        return responseDtoList;
    }


    public ChatRoomResponseDto createChatRoomDto(ChatRoomUser chatRoomUser) {
        String roomname = chatRoomUser.getName();
        Long roomId = chatRoomUser.getChatRoom().getId();
        String lastMessage;
        LocalDateTime lastTime;
        //마지막
        List<ChatMessage> Messages = chatMessageRepository.findAllByChatRoomOrderByCreatedAt(chatRoomUser.getChatRoom());
        //메시지 없을 때 디폴트
        if (Messages.isEmpty()) {
            lastMessage = "채팅방이 생성 되었습니다.";
            lastTime = LocalDateTime.now();
        } else {
            lastMessage = Messages.get(0).getContent();
            lastTime = Messages.get(0).getCreatedAt();
        }
        return new ChatRoomResponseDto(roomname, roomId, lastMessage, lastTime);

    }


}
