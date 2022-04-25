package com.sparta.hh99finalproject.service;

import com.sparta.hh99finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean isDuplicated(String nickname) {
        return userRepository.existsByNickname(nickname)
    }
}
