package com.example.demo.domain.user.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "탈퇴회원")
public class RequestWithdrawal {



    @Schema(description = "사용자 ID", example = "1", required = true)
    private Long userId;
    @Schema(description = "사용자 이메일", maxLength = 40, example = "example@example.com", required = true)
    private String userEmail;
    @Schema(description = "사용자 비밀번호", maxLength = 100, required = true)
    private String userPassword;
    @Schema(description = "사용자 닉네임", maxLength = 10, required = true)
    private String userNickname;
    @Schema(description = "사용자 주소")
    private String userAddress;
    @Schema(description = "사용자 생년월일", example = "19960221")
    private Integer userBirth;
    @Schema(description = "사용자 전화번호", example = "010-1234-5678")
    private String userPhoneNumber;
    @Schema(description = "회원탈퇴 날짜", pattern = "yyyy:MM:dd HH:mm:ss", required = true)
    private LocalDateTime regDateTime;
}
