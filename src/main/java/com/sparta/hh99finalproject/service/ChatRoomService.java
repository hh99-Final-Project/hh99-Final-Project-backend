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
                ()-> new IllegalArgumentException("상대방이 존재하지 않습니다.")
        );

        // 내 채팅방 상대방 채팅방 같은 채팅방 있는지 확인
        existRoom(userDetails,anotherUser);


        //방 먼저 생성
        ChatRoom room = new ChatRoom();
        chatRoomRepository.save(room);

        //내 방
        ChatRoomUser chatRoomUser = new ChatRoomUser(userDetails.getUser(),anotherUser,room);
        //다른 사람 방
        ChatRoomUser chatRoomAnotherUser = new ChatRoomUser(anotherUser,userDetails.getUser(),room);

        //저장
        chatRoomUserRepository.save(chatRoomUser);
        chatRoomUserRepository.save(chatRoomAnotherUser);
    }

    // 여기
    //해쉬코드? chatroom에 서로 판별할 수 있는 엔티티 추가 스트링

    //이미 방이 존재할 때
    public void existRoom(UserDetailsImpl userDetails,User anotherUser ){
        List<ChatRoomUser> chatRoomUserList = chatRoomUserRepository.findAllByUser(userDetails.getUser());
        List<ChatRoomUser> chatRoomAnotherUserList = chatRoomUserRepository.findAllByUser(anotherUser);
        for(ChatRoomUser chatRoomUser:chatRoomUserList){
            for(ChatRoomUser chatRoomAnotherUser:chatRoomAnotherUserList){
                //객체 자체로 비교?? ...챗룸에 나와 상대방의 채팅 방이라고 알 수 있을 만한 변수
                if(chatRoomUser.getChatRoom().equals(chatRoomAnotherUser.getChatRoom())){
                    throw new IllegalArgumentException("이미 존재하는 방입니다.");}
            }
        }
    }

    public List<ChatRoomResponseDto> getChatRoom(UserDetailsImpl userDetails){
        //user로 챗룸 유저를 찾고>>챗룸 유저에서 채팅방을 찾는다
        //마자
        //마지막나온 메시지 ,내용 ,시간
        List<ChatRoomResponseDto> responseDtoList = new ArrayList<>();
        List<ChatRoomUser> chatRoomUserList = chatRoomUserRepository.findAllByUser(userDetails.getUser());


        for(ChatRoomUser chatRoomUser:chatRoomUserList){

            ChatRoomResponseDto responseDto = createChatRoomDto(chatRoomUser);
            responseDtoList.add(responseDto);

            //정렬
            responseDtoList.sort(Collections.reverseOrder());

        }
        return responseDtoList;
    }



    public ChatRoomResponseDto createChatRoomDto(ChatRoomUser chatRoomUser){
        String roomname = chatRoomUser.getName();
        Long roomId = chatRoomUser.getChatRoom().getId();
        String lastMessage;
        LocalDateTime lastTime;
        //마지막
        List<ChatMessage> Messages = chatMessageRepository.findAllByChatRoomOrderByCreatedAt(chatRoomUser.getChatRoom());
        //메시지 없을 때 디폴트
        if(Messages.isEmpty()){
            lastMessage = "채팅방이 생성 되었습니다.";
            lastTime = LocalDateTime.now();
        }

        else {
            lastMessage = Messages.get(0).getContent();
            lastTime = Messages.get(0).getCreatedAt();
        }
        return new ChatRoomResponseDto(roomname, roomId,lastMessage,lastTime);

    }


}
