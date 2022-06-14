package com.example.demo.domain.board.entity.response;

import com.example.demo.domain.board.entity.Board;
import com.example.demo.domain.board.entity.Image;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResponseImage {


    private Long id;
    private String originName;
    private String savePath;
    private String uuidFileName;
    private Long boardId;
    private LocalDateTime uploadDateTime;

}
