package com.sparta.hh99finalproject.controller;

import com.sparta.hh99finalproject.config.GoogleConfigUtils;
import com.sparta.hh99finalproject.dto.SocialLoginRequestDto;
import com.sparta.hh99finalproject.service.GoogleLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/google")
@RequiredArgsConstructor
public class GoogleLoginController {

    private final GoogleConfigUtils configUtils;
    private final GoogleLoginService googleLoginService;

    @GetMapping(value = "/login")
    public ResponseEntity<Object> requestAuthCodeFromGoogle() {
        return googleLoginService.requestAuthCodeFromGoogle();
    }

//    @GetMapping(value = "/login/redirect")
//    public ResponseEntity redirectGoogleLogin(
//            @RequestParam(value = "code") String authCode
//    ) {
//        return googleLoginService.login(authCode);
//    }
    @PostMapping(value = "/api/login/google")
    public ResponseEntity googleLogin(@RequestBody SocialLoginRequestDto socialLoginRequestDto) {
        return googleLoginService.login(socialLoginRequestDto.getJwtToken());
    }
}