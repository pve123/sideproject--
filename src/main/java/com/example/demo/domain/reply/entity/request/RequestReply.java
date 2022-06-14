package com.example.demo.domain.reply.entity.request;


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
@Schema(description = "댓글")
public class RequestReply {


    @Schema(description = "댓글 번호", example = "1", required = true)
    private Long replyId;
    @Schema(description = "댓글 내용", maxLength = 200, required = true)
    private String comment;
    @Schema(description = "댓글 작성자", maxLength = 100, required = true)
    private String writerNickName;
    @Schema(description = "댓글 타입", example = "0 or 1", required = true)
    private Integer replyType;
    @Schema(description = "댓글 그룹", example = "0, 1, 2, 3 ..... 4", required = true)
    private Integer replyGroup;
    @Schema(description = "등록 날짜", pattern = "yyyy:MM:dd HH:mm:ss", required = true)
    private LocalDateTime regDateTime;
    @Schema(description = "업데이트 날짜", pattern = "yyyy:MM:dd HH:mm:ss", required = false)
    private LocalDateTime updateDateTime;
    @Schema(description = "게시물 번호", example = "1", required = true)
    private Long boardId;
}
