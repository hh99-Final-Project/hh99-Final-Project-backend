package com.sparta.hh99finalproject.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.sparta.hh99finalproject.config.GoogleConfigUtils;
import com.sparta.hh99finalproject.domain.User;
import com.sparta.hh99finalproject.dto.request.GoogleLoginDto;
import com.sparta.hh99finalproject.dto.request.GoogleLoginRequest;
import com.sparta.hh99finalproject.dto.response.GoogleLoginResponse;
import com.sparta.hh99finalproject.repository.UserRepository;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class GoogleLoginService {

    private final GoogleConfigUtils configUtils;
    private final UserRepository userRepository;

    // token을 얻기 위한 code 요청
    public ResponseEntity<Object> requestAuthCodeFromGoogle() {
        String authUrl = configUtils.googleInitUrl();
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

    // 얻은 code를 이용하여 token 요청
    // token을 이용하여 사용자 정보 획득
    public ResponseEntity<GoogleLoginDto> login(String authCode) {
        // HTTP 통신을 위해 RestTemplate 활용
        try {
            String jwtToken = getAccessToken(authCode);
            String email = getGoogleUserInfo(jwtToken);
            User googleUser = registerKakaoUserIfNeeded(email);
            forceLogin(googleUser);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(null);
    }

    private void forceLogin(User googleUser) {
        // toDO: spring security 가 적용되면 아래 내용 주석 풀기
//        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private User registerKakaoUserIfNeeded(String email) {
        User googleUser = userRepository.findByUsername(email).orElse(null);

        if (googleUser == null) {

            // toDo: 랜덤 닉네임 부여
            googleUser = new User(email);
            userRepository.save(googleUser);
        }
        return googleUser;
    }

    private String getGoogleUserInfo(String jwtToken) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        // JWT Token을 전달해 JWT 저장된 사용자 정보 확인
        String requestUrl = UriComponentsBuilder.fromHttpUrl(configUtils.getGoogleAuthUrl() + "/tokeninfo").
            queryParam("id_token", jwtToken).toUriString();

        HttpHeaders headers = new HttpHeaders();
        String body = "";
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
            requestUrl,
            HttpMethod.GET,
            requestEntity,
            String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String email = jsonNode.get("email").asText();

        return email;



    }

    private String getAccessToken(String authCode) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        GoogleLoginRequest requestParams = GoogleLoginRequest.builder()
            .clientId(configUtils.getGoogleClientId())
            .clientSecret(configUtils.getGoogleSecret())
            .code(authCode)
            .redirectUri(configUtils.getGoogleRedirectUri())
            .grantType("authorization_code")
            .build();

        // Http Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GoogleLoginRequest> httpRequestEntity = new HttpEntity<>(requestParams, headers);
        ResponseEntity<String> apiResponseJson = restTemplate.postForEntity(configUtils.getGoogleAuthUrl() + "/token", httpRequestEntity, String.class);

        // ObjectMapper를 통해 String to Object로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // NULL이 아닌 값만 응답받기(NULL인 경우는 생략)
        GoogleLoginResponse googleLoginResponse = objectMapper.readValue(apiResponseJson.getBody(), new TypeReference<GoogleLoginResponse>() {});

        // 사용자의 정보는 JWT Token으로 저장되어 있고, Id_Token에 값을 저장한다.
        return googleLoginResponse.getIdToken();
    }

}
