package com.example.demo.domain.board.repository;

import com.example.demo.domain.board.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    void deleteByBoard_BoardId(Long boardId);
}
