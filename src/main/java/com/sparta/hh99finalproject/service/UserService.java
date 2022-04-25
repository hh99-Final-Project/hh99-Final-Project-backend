package com.sparta.hh99finalproject.service;

import com.sparta.hh99finalproject.dto.UserInfoDto;
import com.sparta.hh99finalproject.repository.UserRepository;
import com.sparta.hh99finalproject.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean isDuplicated(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    //로그인한 유저 정보 가져오기
    public UserInfoDto isLogin(UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();
        String nickname = userDetails.getUser().getNickname();

        return new UserInfoDto(userId, nickname);
    }
}
