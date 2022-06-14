package com.example.demo.domain.board.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity(name = "board_views")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoardViews {

    @Id
    @Column(columnDefinition = "VARCHAR(10) comment '유저아이디_게시물번호'", nullable = false)
    private String viewKey;
    @Column(columnDefinition = "DATETIME comment '게시물 조회 날짜'", nullable = false)
    private LocalDateTime viewDateTime;
}
