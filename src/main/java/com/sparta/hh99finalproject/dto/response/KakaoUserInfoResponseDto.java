package com.sparta.hh99finalproject.dto.response;


import com.sparta.hh99finalproject.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserInfoResponseDto {

    private String username;

    public KakaoUserInfoResponseDto(User kakaoUser) {
        this.username = kakaoUser.getUsername();
    }
}
