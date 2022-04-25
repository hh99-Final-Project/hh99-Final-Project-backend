package com.sparta.hh99finalproject.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class ChatRoomResponseDto implements Comparable<ChatRoomResponseDto> {
    private String roomname;
    private Long roomId;
    private String lastMessage;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;

    public ChatRoomResponseDto(String roomname, Long roomId, String lastMessage,LocalDateTime lastTime ) {
        this.roomname = roomname;
        this.roomId=roomId;
        this.lastMessage=lastMessage;
        this.createAt=lastTime;
    }

    @Override
    public int compareTo(ChatRoomResponseDto responseDto){
        if(responseDto.createAt.isBefore(createAt)){
            return 1;
        }else if(responseDto.createAt.isAfter(createAt)){
            return -1;
        }
        return 0;
    }
}
