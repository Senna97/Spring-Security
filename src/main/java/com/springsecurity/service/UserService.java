package com.springsecurity.service;

import com.springsecurity.domain.User;
import com.springsecurity.domain.dto.UserDto;
import com.springsecurity.domain.dto.UserJoinRequest;
import com.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserDto join(UserJoinRequest userJoinRequest) {

        // 회원 userName(id) 중복 Check
        // 중복이면 회원가입 X -> Exception(예외 발생)
        userRepository.findByUserName(userJoinRequest.getUserName())
                .ifPresent(user -> {
                    throw new RuntimeException("입력하신 userName 은 중복으로 회원가입이 불가합니다.");
                });

        // 회원가입: .save()
        User savedUser = userRepository.save(userJoinRequest.toEntity(encoder.encode(userJoinRequest.getPassword())));


        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .emailAddress(savedUser.getEmailAddress())
                .build();
    }
}