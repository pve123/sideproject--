package com.example.demo.domain.board.repository;

import com.example.demo.domain.board.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Integer deleteByBoard_BoardIdAndIdIn(Long boardId, List<Long> imageId);
}
