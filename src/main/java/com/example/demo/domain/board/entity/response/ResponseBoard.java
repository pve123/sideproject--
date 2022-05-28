package com.example.demo.domain.board.entity.response;

import com.example.demo.domain.board.entity.Board;
import com.example.demo.domain.board.entity.Image;
import com.example.demo.domain.user.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class ResponseBoard {


    private Long boardId;
    private String subject;
    private String content;
    private List<Image> images;
    private String writerNickName;
    private String writerEmail;
    private Integer views;
    private LocalDateTime regDateTime;
    private LocalDateTime updateDateTime;
    private Long userId;


    public ResponseBoard(Board board, User user) {
        BeanUtils.copyProperties(board, this);
        this.userId = user.getUserId();
    }

    public ResponseBoard() {

    }
}
