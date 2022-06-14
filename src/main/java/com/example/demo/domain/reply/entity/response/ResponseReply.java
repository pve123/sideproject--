package com.example.demo.domain.reply.entity.response;

import com.example.demo.domain.reply.entity.Reply;
import com.example.demo.domain.reply.entity.request.RequestReply;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
public class ResponseReply {


    private Long replyId;
    private String comment;
    private String writerNickName;
    private Integer replyType;
    private Integer replyGroup;
    private LocalDateTime regDateTime;
    private LocalDateTime updateDateTime;
    private Long boardId;
    private Integer reCommentCnt;


    public ResponseReply(Reply reply) {
        BeanUtils.copyProperties(reply, this);
        this.boardId = reply.getBoard().getBoardId();
    }
    public ResponseReply() {

    }
}
