package com.example.demo.domain.reply.entity;

import com.example.demo.domain.board.entity.Board;
import com.example.demo.domain.reply.entity.request.RequestReply;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "reply")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Reply {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;
    @Column(columnDefinition = "VARCHAR(200) comment '댓글 내용'", nullable = false)
    private String comment;
    @Column(columnDefinition = "VARCHAR(100) comment '댓글 작성자'", nullable = false)
    private String writerNickName;
    @Column(columnDefinition = "INT comment '메인댓글(0) / 재댓글(1)'", nullable = false)
    private Integer replyType;
    @Column(columnDefinition = "INT comment '댓글 그룹'", nullable = false)
    private Integer replyGroup;
    @Column(columnDefinition = "DATETIME comment '댓글 등록날짜'", nullable = false)
    private LocalDateTime regDateTime;
    @Column(columnDefinition = "DATETIME comment '댓글 수정날짜'", nullable = true)
    private LocalDateTime updateDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", columnDefinition = "BIGINT", nullable = false, referencedColumnName = "boardId"
            , foreignKey = @ForeignKey(name = "FK_REPLY_BOARD_ID",
            foreignKeyDefinition = "FOREIGN KEY(board_id) REFERENCES BOARD(board_id) ON DELETE CASCADE"))
    private Board board;


    public Reply(RequestReply requestReply, Board board) {
        BeanUtils.copyProperties(requestReply, this);
        this.board = board;
    }

}