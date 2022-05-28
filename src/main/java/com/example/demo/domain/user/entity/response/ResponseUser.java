package com.example.demo.domain.user.entity.response;

import com.example.demo.domain.user.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.time.LocalDateTime;

@Data
public class ResponseUser {

    private Long userId;
    private String userEmail;
    private String userPassword;
    private String userNickname;
    private String userAddress;
    private Integer userBirth;
    private String userPhoneNumber;
    private LocalDateTime regDateTime;
    private LocalDateTime updateDateTime;
    private String userAuthority;
    private String refreshToken;


    @ReadOnlyProperty
    private String grantType;
    @ReadOnlyProperty
    private String accessToken;
    @ReadOnlyProperty
    private Long accessTokenExpiresIn;


    public ResponseUser(User user) {
        BeanUtils.copyProperties(user, this);
    }

    public ResponseUser(){

    }
}
