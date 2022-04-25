package com.sparta.hh99finalproject.controller;

import com.sparta.hh99finalproject.dto.ChatRoomResponseDto;
import com.sparta.hh99finalproject.dto.ChatRoomUserRequestDto;
import com.sparta.hh99finalproject.security.UserDetailsImpl;
import com.sparta.hh99finalproject.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    //방생성
    @PostMapping("/rooms")
    public void createChatRoom(@RequestBody ChatRoomUserRequestDto requestDto,@AuthenticationPrincipal UserDetailsImpl userDetails){
        chatRoomService.creatChatRoom(requestDto, userDetails);
    }

    //내가 가진 채팅방 조회
    @GetMapping("/rooms")
    public List<ChatRoomResponseDto> getChatRoom(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return chatRoomService.getChatRoom(userDetails);
    }



}
