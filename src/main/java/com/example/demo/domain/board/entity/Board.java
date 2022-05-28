package com.example.demo.domain.board.entity;


import com.example.demo.domain.board.entity.request.RequestBoard;
import com.example.demo.domain.user.entity.User;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;
    @Column(columnDefinition = "VARCHAR(100) comment '게시물 제목'", nullable = false)
    private String subject;
    @Column(columnDefinition = "TEXT comment '게시물 내용'", nullable = false)
    private String content;
    @Column(columnDefinition = "VARCHAR(100) comment '글쓴이 닉네임'", nullable = false)
    private String writerNickName;
    @Column(columnDefinition = "VARCHAR(100) comment '글쓴이 이메일'", nullable = false)
    private String writerEmail;
    @Column(columnDefinition = "INT comment '게시글 조회수'", nullable = true)
    private Integer views;
    @Column(columnDefinition = "DATETIME comment '게시물 등록 날짜'", nullable = false)
    private LocalDateTime regDateTime;
    @Column(columnDefinition = "DATETIME comment '게시물 수정 날짜'", nullable = true)
    private LocalDateTime updateDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "BIGINT", nullable = false, referencedColumnName = "userId"
            , foreignKey = @ForeignKey(name = "FK_BOARD_USER_ID"))
    private User user;


    @OneToMany(mappedBy = "board")
    private List<Image> images;

    public Board(RequestBoard requestBoard, User user) {
        BeanUtils.copyProperties(requestBoard, this);
        this.user = user;
    }


}
