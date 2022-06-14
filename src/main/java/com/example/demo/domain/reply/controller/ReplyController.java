package com.example.demo.domain.reply.controller;


import com.example.demo.domain.reply.entity.request.RequestReply;
import com.example.demo.domain.reply.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping(path = "/reply")
    @Operation(summary = "댓글 또는 재댓글 작성", description = "댓글 또는 재댓글 작성 API", responses = {
            @ApiResponse(responseCode = "201", description = "댓글 또는 재댓글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "댓글 또는 재댓글 작성 실패")})
    public ResponseEntity addReply(@RequestBody RequestReply requestReply) {

        return ResponseEntity.status(HttpStatus.CREATED).body(replyService.addReply(requestReply));
    }


    @GetMapping(path = "/reply")
    @Operation(summary = "댓글 목록 페이징", description = "댓글 목록 페이징 API", responses = {
            @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공")})
    public ResponseEntity pagingBoardReply(@RequestParam(defaultValue = "1", value = "page") Integer pageNum,
                                           @RequestParam(defaultValue = "10", value = "size") Integer pageSize,
                                           @RequestParam("boardId") Long boardId) {

        return ResponseEntity.status(HttpStatus.OK).body(replyService.pagingBoardReply(pageNum, pageSize, boardId));
    }
    @GetMapping(path = "/reply/recomment")
    @Operation(summary = "대댓글 목록 페이징", description = "대댓글 목록 페이징 API", responses = {
            @ApiResponse(responseCode = "200", description = "대댓글 목록 조회 성공")})
    public ResponseEntity pagingBoardReply(
            @RequestParam(defaultValue = "1", value = "page") Integer pageNum,
            @RequestParam(defaultValue = "10", value = "size") Integer pageSize,
            @RequestParam("replyGroup") Integer replyGroup) {

        return ResponseEntity.status(HttpStatus.OK).body(replyService.pagingReplyRecomment(pageNum, pageSize, replyGroup));
    }

}
