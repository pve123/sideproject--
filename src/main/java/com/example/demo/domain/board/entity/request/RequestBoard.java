package com.example.demo.domain.board.entity.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "게시글")
public class RequestBoard {


    @Schema(description = "게시물 번호")
    private Long boardId;
    @Schema(description = "게시물 제목")
    private String subject;
    @Schema(description = "게시물 내용")
    private String content;
    @Schema(description = "게시물 이미지")
    private String images;
    @Schema(description = "글쓴이 닉네임")
    private String writerNickName;
    @Schema(description = "글쓴이 이메일")
    private String writerEmail;
    @Schema(description = "게시글 조회수")
    private Integer views;
    @Schema(description = "게시물 등록 날짜")
    private LocalDateTime regDateTime;
    @Schema(description = "게시물 수정 날짜")
    private LocalDateTime updateDateTime;
    @Schema(description = "유저 고유번호")
    private Long userId;

}
