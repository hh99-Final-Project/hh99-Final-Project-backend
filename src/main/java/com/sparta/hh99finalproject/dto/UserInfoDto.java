package com.sparta.hh99finalproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDto {

    private Long userId;
    private String nickname;

    public UserInfoDto(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }
}
