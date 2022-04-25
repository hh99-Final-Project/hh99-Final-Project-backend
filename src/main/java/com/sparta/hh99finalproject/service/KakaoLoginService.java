package com.sparta.hh99finalproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.hh99finalproject.config.KakaoConfigUtils;
import com.sparta.hh99finalproject.domain.User;
import com.sparta.hh99finalproject.repository.UserRepository;
import com.sparta.hh99finalproject.security.UserDetailsImpl;
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
import org.springframework.security.core.userdetails.UserDetails;
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
    public void login(String authCode) {
        // HTTP 통신을 위해 RestTemplate 활용
        try {
            String jwtToken = getAccessToken(authCode);
            // 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
            String email = getKakaoUserInfo(jwtToken);
            // 3. "카카오 사용자 정보"로 필요시 회원가입
            User kakaoUser = registerKakaoUserIfNeeded(email);
            // 4. 강제 로그인 처리
            forceLogin(kakaoUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return ResponseEntity.badRequest().body(null);
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
// HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

// HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.POST,
            kakaoUserInfoRequest,
            String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String email = jsonNode.get("kakao_account")
            .get("email").asText();

        return email;
    }

    private User registerKakaoUserIfNeeded(String email) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        User kakaoUser = userRepository.findByUsername(email)
            .orElse(null);
        if (kakaoUser == null) {
            // 회원가입

            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            kakaoUser = new User(email, encodedPassword);
            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }

    private void forceLogin(User kakaoUser) {
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}