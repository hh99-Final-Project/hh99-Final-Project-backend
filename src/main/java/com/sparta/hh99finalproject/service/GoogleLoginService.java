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
import com.sparta.hh99finalproject.dto.response.SocialLoginResponseDto;
import com.sparta.hh99finalproject.repository.UserRepository;
import com.sparta.hh99finalproject.security.jwt.JwtTokenUtils;
import java.net.URI;
import java.net.URISyntaxException;

import com.sparta.hh99finalproject.security.UserDetailsImpl;
import java.util.UUID;

import com.sparta.hh99finalproject.security.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class GoogleLoginService {

    private final GoogleConfigUtils configUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // token??? ?????? ?????? code ??????
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

    // ?????? code??? ???????????? token ??????
    // token??? ???????????? ????????? ?????? ??????
    public ResponseEntity login(String jwtToken) {
        // HTTP ????????? ?????? RestTemplate ??????
        try {
//            String jwtToken = getAccessToken(authCode);
            System.out.println("jwtToken = " + jwtToken);
            String email = getGoogleUserInfo(jwtToken);
            User googleUser = registerKakaoUserIfNeeded(email);
            return forceLogin(googleUser);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body(null);
    }

    private ResponseEntity forceLogin(User googleUser) {
        UserDetailsImpl userDetails = new UserDetailsImpl(googleUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // toDo: ?????? ????????? ??????
        String nickname = "????????? ????????? ???????????????";
        googleUser.createNickname(nickname);

        // Token ??????
        final String token = JwtTokenUtils.generateJwtToken(userDetails);
        System.out.println("token = " + token);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+token);

        return ResponseEntity.ok()
            .headers(headers)
            .body(new SocialLoginResponseDto(googleUser));
    }

    private User registerKakaoUserIfNeeded(String email) {
        User googleUser = userRepository.findByUsername(email).orElse(null);

        if (googleUser == null) {

            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            // toDo: ?????? ????????? ??????
            googleUser = new User(email, encodedPassword);
            userRepository.save(googleUser);
        }
        return googleUser;
    }

    private String getGoogleUserInfo(String jwtToken) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        // JWT Token??? ????????? JWT ????????? ????????? ?????? ??????
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

        // Http Header ??????
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GoogleLoginRequest> httpRequestEntity = new HttpEntity<>(requestParams, headers);
        ResponseEntity<String> apiResponseJson = restTemplate.postForEntity(configUtils.getGoogleAuthUrl() + "/token", httpRequestEntity, String.class);

        // ObjectMapper??? ?????? String to Object??? ??????
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // NULL??? ?????? ?????? ????????????(NULL??? ????????? ??????)
        GoogleLoginResponse googleLoginResponse = objectMapper.readValue(apiResponseJson.getBody(), new TypeReference<GoogleLoginResponse>() {});

        // ???????????? ????????? JWT Token?????? ???????????? ??????, Id_Token??? ?????? ????????????.
        return googleLoginResponse.getIdToken();
    }

}
