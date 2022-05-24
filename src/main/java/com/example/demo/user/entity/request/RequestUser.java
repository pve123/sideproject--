package com.example.demo.user.entity.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "사용자")
public class RequestUser {


    @Schema(description = "사용자 ID",example = "1")
    private Long userId;
    @Schema(description = "사용자 이메일", maxLength = 40, example = "example@example.com")
    private String userEmail;
    @Schema(description = "사용자 비밀번호", maxLength = 100)
    private String userPassword;
    @Schema(description = "사용자 닉네임", maxLength = 10)
    private String userNickname;
    @Schema(description = "사용자 주소")
    private String userAddress;
    @Schema(description = "사용자 생년월일", example = "19960221")
    private Integer userBirth;
    @Schema(description = "사용자 전화번호", example = "010-1234-5678")
    private String userPhoneNumber;
    @Schema(description = "생성날짜", pattern = "yyyy:MM:dd HH:mm:ss")
    private LocalDateTime regDateTime;
    @Schema(description = "업데이트 날짜", pattern = "yyyy:MM:dd HH:mm:ss")
    private LocalDateTime updateDateTime;
    @Schema(description = "사용자 권한", example = "ROLE_USER")
    private String userAuthority;
    @Schema(description = "사용자 Refresh Token")
    private String refreshToken;

    @ReadOnlyProperty
    @Schema(description = "인증 타입")
    private String grantType;
    @ReadOnlyProperty
    @Schema(description = "사용자 Access Token")
    private String accessToken;
    @ReadOnlyProperty
    @Schema(description = "사용자 Access Token Expired Time")
    private Long accessTokenExpiresIn;


    public RequestUser toMember(PasswordEncoder passwordEncoder) {
        Maps.newHashMap();
        return RequestUser.builder()
                .userEmail(userEmail)
                .userNickname(userNickname)
                .userPassword(passwordEncoder.encode(userPassword))
                .userAuthority("ROLE_USER")
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(userEmail, userPassword);
    }
}
