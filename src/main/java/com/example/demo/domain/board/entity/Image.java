package com.example.demo.domain.board.entity;


import com.example.demo.domain.board.entity.request.RequestBoard;
import com.example.demo.domain.user.entity.User;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Image {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(100) comment '업로드 이미지 이름'", nullable = false)
    private String originName;
    @Column(columnDefinition = "VARCHAR(255) comment '이미지 저장 경로'", nullable = false)
    private String savePath;
    @Column(columnDefinition = "VARCHAR(100) comment '서버에 올라가는 이미지 이름'", nullable = false)
    private String uuidFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", columnDefinition = "BIGINT", nullable = false, referencedColumnName = "boardId"
            , foreignKey = @ForeignKey(name = "FK_IMAGE_BOARD_ID",
            foreignKeyDefinition = "FOREIGN KEY(board_id) REFERENCES BOARD(board_id) ON DELETE CASCADE"))
    private Board board;


    public Image(Image image, Board board) {
        BeanUtils.copyProperties(image, this);
        this.board = board;
    }

}
