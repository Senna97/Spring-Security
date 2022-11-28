package com.springsecurity.service;

import com.springsecurity.domain.dto.UserDto;
import com.springsecurity.domain.dto.UserJoinRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public UserDto join(UserJoinRequest userJoinRequest) {
        return new UserDto();
    }
}
