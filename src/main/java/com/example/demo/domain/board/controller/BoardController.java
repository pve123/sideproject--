package com.example.demo.domain.board.controller;


import com.example.demo.domain.board.entity.Image;
import com.example.demo.domain.board.entity.request.RequestBoard;
import com.example.demo.domain.board.entity.request.RequestImage;
import com.example.demo.domain.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BoardController {

    private final BoardService boardService;

    @PostMapping(path = "/board")
    @Operation(summary = "게시글 작성", description = "게시글 작성 API", responses = {
            @ApiResponse(responseCode = "201", description = "게시글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "게시글 작성 실패")})
    public ResponseEntity addBoard(@RequestBody RequestBoard requestBoard) {

        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.addBoard(requestBoard));
    }

    @PostMapping(path = "/board/images")
    @Operation(summary = "이미지 추가", description = "게시물 이미지 추가 API", responses = {
            @ApiResponse(responseCode = "201", description = "이미지 추가 성공"),
            @ApiResponse(responseCode = "400", description = "이미지 추가 실패")})
    public ResponseEntity uploadImages(MultipartFile[] uploadFiles, Long boardId) {


        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.uploadImages(uploadFiles, boardId));
    }

    @DeleteMapping(path = "/board/images")
    @Operation(summary = "이미지 삭제", description = "게시물 이미지 삭제 API", responses = {
            @ApiResponse(responseCode = "200", description = "이미지 추가 성공")})
    public ResponseEntity removeImages(@RequestBody RequestImage requestImages) {

        return ResponseEntity.status(HttpStatus.OK).body(boardService.removeImages(requestImages));
    }


    @GetMapping(path = "/board")
    @Operation(summary = "게시물 목록", description = "게시물 목록 페이징 API", responses = {
            @ApiResponse(responseCode = "200", description = "게시물 목록조회 성공")})
    public ResponseEntity pagingTotalBoard(
            @RequestParam(defaultValue = "1", value = "page") Integer pageNum,
            @RequestParam(defaultValue = "5", value = "size") Integer pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.pagingTotalBoard(pageNum, pageSize));
    }

    @GetMapping(path = "/board/{id}")
    @Operation(summary = "해당 게시물 조회", description = "게시물 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "게시물 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글 조회 실패")})
    public ResponseEntity findBoard(@PathVariable("id") Long boardId) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.findBoard(boardId));
    }

    @GetMapping(path = "/board/user/{id}")
    @Operation(summary = "유저 게시물 조회", description = "유저 게시물 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 게시물 조회 성공")})
    public ResponseEntity findUserBoard(@PathVariable("id") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.findUserBoard(userId));
    }

    @PutMapping(path = "/board")
    @Operation(summary = "유저 게시물 수정", description = "유저 게시물 수정 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 게시물 수정 성공"),
            @ApiResponse(responseCode = "400", description = "유저 게시물 수정 실패")})
    public ResponseEntity updateBoard(@RequestBody RequestBoard requestBoard) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.updateBoard(requestBoard));
    }

    @DeleteMapping(path = "/board/{id}")
    @Operation(summary = "유저 게시물 삭제", description = "유저 게시물 삭제 API", responses = {
            @ApiResponse(responseCode = "200", description = "유저 게시물 삭제 성공")})
    public ResponseEntity deleteBoard(@PathVariable("id") Long boardId) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.deleteBoard(boardId));
    }


}
