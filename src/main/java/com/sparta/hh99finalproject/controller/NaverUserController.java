package com.sparta.hh99finalproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.hh99finalproject.service.NaverUserService;
import java.io.BufferedReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class NaverUserController {

    private final NaverUserService naverUserService;

    @GetMapping("/user/naver/callback")
    public String naverLogin(@RequestParam String code) throws JsonProcessingException {
//        naverUserService.kakaoLogin(code);
        System.out.println(code);
//        return "index";

        // 코드 요청
        // https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=Ke0JA3iz0dHP_2GWHiyU&redirect_uri=http://localhost:8080/user/naver/callback&state=130
        // 발급 받은 code: nzY9XzEXP6DzzoOLqv

        // 발급 받은 코드를 이용하여 토큰 요청
        // https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id=Ke0JA3iz0dHP_2GWHiyU&client_secret=12tLr7TqNK&code=nzY9XzEXP6DzzoOLqv&state=130
//        {
//            "access_token": "AAAAOTfNhwklAZQTNvS8I7w1T1hNzBlCeWFshfgxlb2Q7zQ_ktRjBe-OsI-45GitRSAF9lBoGSb4XZHO2e7t3golhsE",
//            "refresh_token": "o5DyrEmFgWyQmZy9PHXNueTZnQV06RXTNNYHt8BXwLLlyqVELg0nr3Y3BCisx837OmHSrBJh57bnoEeQMU4rCpwkIHzQ1JB92K247G0R2A4JUJaeNlD4Y0lfRjr3PrWfJ",
//            "token_type": "bearer",
//            "expires_in": "3600"
//        }

        String token = "AAAAOTfNhwklAZQTNvS8I7w1T1hNzBlCeWFshfgxlb2Q7zQ_ktRjBe-OsI-45GitRSAF9lBoGSb4XZHO2e7t3golhsE";
        naverUserService.login(token);

        // 토큰 요청 코드
//        URL url = new URL("https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id=Ke0JA3iz0dHP_2GWHiyU&client_secret=12tLr7TqNK&code=nzY9XzEXP6DzzoOLqv&state=130");
//        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
//        con.setRequestMethod("GET");
//
//        int status = con.getResponseCode();
//        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuffer content = new StringBuffer();
//        while((inputLine = in.readLine()) != null) {
//            content.append(inputLine);
//        }
//        in.close();
//        con.disconnect();
//        System.out.println("Response status: " + status);
//        System.out.println(content.toString());

        return "index";



    }
}
