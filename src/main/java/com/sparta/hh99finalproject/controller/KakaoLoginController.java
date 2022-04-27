package com.sparta.hh99finalproject.controller;

import com.sparta.hh99finalproject.service.KakaoLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoLoginService kakoLoginService;

    @GetMapping(value = "/kakao/login")
    public ResponseEntity<Object> requestAuthCodeFromKakao() {
        return kakoLoginService.requestAuthCodeFromKakao();
    }

    @GetMapping(value = "/user/kakao/callback")
//    public ResponseEntity<KakaoLoginService> redirectKakaoLogin(
    public ResponseEntity redirectKakaoLogin(@RequestParam(value = "code") String authCode) {
        return kakoLoginService.login(authCode);
    }
}