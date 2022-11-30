package com.springsecurity.service;

import com.springsecurity.domain.User;
import com.springsecurity.domain.dto.UserDto;
import com.springsecurity.domain.dto.UserJoinRequest;
import com.springsecurity.exception.ErrorCode;
import com.springsecurity.exception.SpringSecurityAppException;
import com.springsecurity.repository.UserRepository;
import com.springsecurity.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private long expireTimeMs = 1000 * 60 * 60;

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

    public String login(String userName, String password) {
        // userName 있는지 여부 확인 => 없으면 NOT_FOUND 예외 발생
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SpringSecurityAppException(ErrorCode.NOT_FOUND, String.format("%s는 없는 아이디입니다.", userName)));

        // password 맞는지 여부 확인
        if (!encoder.matches(password, user.getPassword())) {
            throw new SpringSecurityAppException(ErrorCode.INVALID_PASSWORD, String.format("틀린 비밀번호입니다.", userName));
        }

        // 두가지 모두 예외가 나지 않으면 토큰 발행
        return JwtTokenUtil.createToken(userName, secretKey, expireTimeMs);
    }
}