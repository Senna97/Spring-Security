package com.springsecurity.domain.dto;

import com.springsecurity.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {
    private String userName;
    private String password;
    private String emailAddress;

    public User toEntity() {
        return User.builder()
                .userName(this.userName)
                .password(this.password)
                .emailAddress(this.emailAddress)
                .build();
    }
}
