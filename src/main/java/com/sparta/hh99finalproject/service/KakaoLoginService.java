package com.sparta.hh99finalproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hh99finalproject.config.KakaoConfigUtils;
import com.sparta.hh99finalproject.domain.User;
import com.sparta.hh99finalproject.dto.SocialLoginRequestDto;
import com.sparta.hh99finalproject.dto.response.SocialLoginResponseDto;
import com.sparta.hh99finalproject.repository.UserRepository;
import com.sparta.hh99finalproject.security.UserDetailsImpl;
import com.sparta.hh99finalproject.security.jwt.JwtTokenUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    private final KakaoConfigUtils configUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<Object> requestAuthCodeFromKakao() {
        String authUrl = configUtils.kakaoInitUrl();
        System.out.println("authUrl = " + authUrl);
        URI redirectUri = null;
        try {
            redirectUri = new URI(authUrl);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUri);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().build();
    }

//    public ResponseEntity<KakaoLoginDto> login(String authCode) {
    public ResponseEntity login(String jwtToken) {
        // HTTP ????????? ?????? RestTemplate ??????
        try {
//            String jwtToken = getAccessToken(authCode);
            System.out.println("jwtToken = " + jwtToken);
            // 2. "????????? ??????"?????? "????????? ????????? ??????" ????????????
            String nickname = getKakaoUserInfo(jwtToken);
            // 3. "????????? ????????? ??????"??? ????????? ????????????
            User kakaoUser = registerKakaoUserIfNeeded(nickname);
            // 4. ?????? ????????? ??????
            return forceLogin(kakaoUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(null);
    }



//    private String getAccessToken(String authCode) throws JsonProcessingException {
    private String getAccessToken(String authCode) throws URISyntaxException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = configUtils.getKakaoTokenUrl();
        URI requestUrl = new URI(tokenUrl);

        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> body = configUtils.kakaoTokenBody(authCode);
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
            new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            requestUrl,
            HttpMethod.POST,
            kakaoTokenRequest,
            String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private String getKakaoUserInfo(String accessToken) throws JsonProcessingException {
// HTTP Header ??????
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

// HTTP ?????? ?????????
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.POST,
            kakaoUserInfoRequest,
            String.class
        );

        String responseBody = response.getBody();
        System.out.println(responseBody);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String nickname = jsonNode.get("properties")
            .get("nickname").asText();
        return nickname;
    }

    private User registerKakaoUserIfNeeded(String nickname) {
        // DB ??? ????????? Kakao Id ??? ????????? ??????
        User kakaoUser = userRepository.findByUsername(nickname)
            .orElse(null);
        if (kakaoUser == null) {
            // ????????????

            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            kakaoUser = new User(nickname, encodedPassword);
            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }

    private ResponseEntity forceLogin(User kakaoUser) {
        UserDetailsImpl userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // toDo: random Nickname ????????? ?????????
        String nickname = "????????? ????????? ???????????????";
        kakaoUser.createNickname(nickname);

        // Token ??????
        final String token = JwtTokenUtils.generateJwtToken(userDetails);
        System.out.println("token = " + token);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+token);
        return ResponseEntity.ok()
            .headers(headers)
            .body(new SocialLoginResponseDto(kakaoUser));
    }
}
