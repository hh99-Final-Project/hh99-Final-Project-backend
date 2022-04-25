package com.sparta.hh99finalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter

public class ChatRoomUserRequestDto {

    private String roomName;
    private Long userId;// 상대방


}
