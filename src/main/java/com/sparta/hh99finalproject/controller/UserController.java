package com.sparta.hh99finalproject.controller;

import com.sparta.hh99finalproject.dto.request.NicknameCheckRequestDto;
import com.sparta.hh99finalproject.dto.response.UserInfoDto;
import com.sparta.hh99finalproject.security.UserDetailsImpl;
import com.sparta.hh99finalproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //로그인한 유저 정보 가져오기
    @GetMapping("/api/user/islogin")
    public UserInfoDto isLogin(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.isLogin(userDetails);
    }

    // 닉네임 중복 확인
    @GetMapping("/api/nicknames")
    public boolean isDuplicated(@RequestBody NicknameCheckRequestDto nicknameCheckRequestDto) {
        return userService.isDuplicated(nicknameCheckRequestDto.getNickname());
    }
}
