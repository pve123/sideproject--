package com.example.demo.domain.reply.service;


import com.example.demo.domain.board.entity.Board;
import com.example.demo.domain.board.entity.QBoard;
import com.example.demo.domain.reply.entity.QReply;
import com.example.demo.domain.reply.entity.Reply;
import com.example.demo.domain.reply.entity.request.RequestReply;
import com.example.demo.domain.reply.entity.response.ResponseReply;
import com.example.demo.domain.reply.repository.ReplyRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final JPAQueryFactory queryFactory;

    public ResponseReply addReply(RequestReply requestReply) {

        QReply qReply = QReply.reply;
        QBoard qBoard = QBoard.board;
        Board board = queryFactory.selectFrom(qBoard)
                .where(qBoard.boardId.eq(requestReply.getBoardId()))
                .fetchOne();
        if (board == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 작성에 실패하셨습니다.");
        Long replyCnt = replyRepository.count();

        if (requestReply.getReplyGroup() != null) {

        } else if (replyCnt < 1) {
            requestReply.setReplyGroup(0);
        } else {
            Reply reply = queryFactory.selectFrom(qReply)
                    .orderBy(qReply.regDateTime.desc())
                    .limit(1)
                    .fetchOne();
            requestReply.setReplyGroup(reply.getReplyGroup() + 1);
        }
        requestReply.setRegDateTime(LocalDateTime.now());
        Reply result = new Reply(requestReply, board);
        replyRepository.save(result);
        ResponseReply responseReply = new ResponseReply(result);
        return responseReply;
    } // 댓글 또는 재댓글 작성

    public Object pagingBoardReply(Integer pageNum, Integer pageSize, Long boardId) {
        QReply qReply = QReply.reply;
        QBoard qBoard = QBoard.board;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        List<ResponseReply> responseReplies = queryFactory.select(Projections.fields(
                ResponseReply.class,
                qReply.board.boardId,
                qReply.comment,
                qReply.replyId,
                qReply.replyGroup,
                qReply.replyType,
                qReply.writerNickName,
                qReply.regDateTime,
                qReply.updateDateTime
        ))
                .from(qBoard)
                .innerJoin(qBoard.replies, qReply)
                .on(qBoard.boardId.eq(boardId))
                .where(qReply.replyType.eq(0))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        for (ResponseReply responseReply : responseReplies) {
            List<Reply> replies = replyRepository.findByReplyGroupAndReplyType(responseReply.getReplyGroup(), 1);

            responseReply.setReCommentCnt(replies.size());
        }


        return responseReplies;
    } //게시물 댓글 페이징


    public Object pagingReplyRecomment(Integer pageNum, Integer pageSize, Integer replyGroup) {
        QReply qReply = QReply.reply;
        QBoard qBoard = QBoard.board;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        List<ResponseReply> responseReplies = queryFactory.select(Projections.fields(
                ResponseReply.class,
                qReply.board.boardId,
                qReply.comment,
                qReply.replyId,
                qReply.replyGroup,
                qReply.replyType,
                qReply.writerNickName,
                qReply.regDateTime,
                qReply.updateDateTime
        ))
                .from(qBoard)
                .innerJoin(qBoard.replies, qReply)
                .where(qReply.replyType.eq(1).and(qReply.replyGroup.eq(replyGroup)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return responseReplies;
    } //대댓글 페이징
}
