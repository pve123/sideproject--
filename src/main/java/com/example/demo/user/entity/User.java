package com.example.demo.user.entity;


import com.example.demo.user.entity.request.RequestUser;
import com.example.demo.user.entity.response.ResponseUser;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(columnDefinition = "VARCHAR(40) comment '사용자 이메일'", nullable = false)
    private String userEmail;
    @Column(columnDefinition = "VARCHAR(100) comment '사용자 비밀번호'", nullable = false)
    private String userPassword;
    @Column(columnDefinition = "VARCHAR(10) comment '사용자 닉네임'", nullable = false)
    private String userNickname;
    @Column(columnDefinition = "VARCHAR(100) comment '사용자 주소'", nullable = true)
    private String userAddress;
    @Column(columnDefinition = "INT comment '사용자 생년월일'", nullable = true)
    private Integer userBirth;
    @Column(columnDefinition = "VARCHAR(14) comment '사용자 핸드폰번호'", nullable = true)
    private String userPhoneNumber;
    @Column(columnDefinition = "VARCHAR(10) comment '사용자 권한'", nullable = false)
    private String userAuthority;
    @Column(columnDefinition = "VARCHAR(255) comment '사용자 ReFresh Token'", nullable = true)
    private String refreshToken;

    @Column(columnDefinition = "DATETIME comment '회원가입 날짜'", nullable = false)
    private LocalDateTime regDateTime;
    @Column(columnDefinition = "DATETIME comment '회원정보 수정 날짜'", nullable = true)
    private LocalDateTime updateDateTime;

    public User(RequestUser requestUser) {
        BeanUtils.copyProperties(requestUser, this);
        if (requestUser.getRegDateTime() == null) {
            this.regDateTime = LocalDateTime.now();
        }
        this.userAuthority = "ROLE_USER";
    }

    public User(ResponseUser responseUser) {
        BeanUtils.copyProperties(responseUser, this);
    }
}
