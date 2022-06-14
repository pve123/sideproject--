package com.example.demo.domain.board.repository;

import com.example.demo.domain.board.entity.Board;
import com.example.demo.domain.board.entity.BoardViews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardViewsRepository extends JpaRepository<BoardViews, String> {

    BoardViews findByViewKey(String viewKey);
}
