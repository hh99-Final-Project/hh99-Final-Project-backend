package com.sparta.hh99finalproject.controller;

import com.sparta.hh99finalproject.config.GoogleConfigUtils;
import com.sparta.hh99finalproject.dto.request.GoogleLoginDto;
import com.sparta.hh99finalproject.service.GoogleLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/google")
@RequiredArgsConstructor
public class GoogleLoginController {

    private final GoogleConfigUtils configUtils;
    private final GoogleLoginService googleLoginService;

    @GetMapping(value = "/login")
    public ResponseEntity<Object> requestAuthCodeFromGoogle() {
        return googleLoginService.requestAuthCodeFromGoogle();
    }

    @GetMapping(value = "/login/redirect")
    public ResponseEntity<GoogleLoginDto> redirectGoogleLogin(
            @RequestParam(value = "code") String authCode
    ) {
        return googleLoginService.login(authCode);
    }
}