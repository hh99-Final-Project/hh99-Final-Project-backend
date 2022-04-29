package com.sparta.hh99finalproject.dto.response;

import com.sparta.hh99finalproject.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialLoginResponseDto {

    private Long userId;
    private String nickname;

    public SocialLoginResponseDto(User kakaoUser) {
        this.userId = kakaoUser.getId();
        this.nickname = kakaoUser.getNickname();
    }
}
