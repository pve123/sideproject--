package com.example.demo.domain.board.entity.request;

import com.example.demo.domain.board.entity.Board;
import com.example.demo.domain.board.entity.Image;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
public class RequestImage {

    private Long boardId;
    private List<Image> imageList;


}
