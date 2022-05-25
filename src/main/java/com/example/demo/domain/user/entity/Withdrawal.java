package com.example.demo.domain.user.entity;

import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity(name = "withdrawal")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Withdrawal {


    @Id
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
    @Column(columnDefinition = "DATETIME comment '회원탈퇴 날짜'", nullable = false)
    private LocalDateTime regDateTime;



    public Withdrawal(User user) {
        BeanUtils.copyProperties(user, this);
    }
}
