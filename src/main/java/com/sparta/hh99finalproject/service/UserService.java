package com.sparta.hh99finalproject.service;

import com.sparta.hh99finalproject.dto.UserInfoDto;
import com.sparta.hh99finalproject.security.UserDetailsImpl;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    //로그인한 유저 정보 가져오기
    public UserInfoDto isLogin(UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        String nickname = userDetails.getUser().getNickname();

        return new UserInfoDto(userId, nickname);
    }
}
