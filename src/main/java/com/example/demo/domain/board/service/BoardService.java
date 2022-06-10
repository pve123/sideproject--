package com.example.demo.domain.board.service;

import com.example.demo.domain.board.entity.Board;
import com.example.demo.domain.board.entity.Image;
import com.example.demo.domain.board.entity.QBoard;
import com.example.demo.domain.board.entity.QImage;
import com.example.demo.domain.board.entity.request.RequestBoard;
import com.example.demo.domain.board.entity.request.RequestImage;
import com.example.demo.domain.board.entity.response.ResponseBoard;
import com.example.demo.domain.board.entity.response.ResponseImage;
import com.example.demo.domain.board.repository.BoardRepository;
import com.example.demo.domain.board.repository.ImageRepository;
import com.example.demo.domain.user.entity.QUser;
import com.example.demo.domain.user.entity.User;
import com.example.demo.exception.ResponseMessage;
import com.example.demo.util.FileUpload;
import com.google.common.collect.Lists;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final JPAQueryFactory queryFactory;
    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;
    private final FileUpload fileUpload;

    public ResponseBoard addBoard(RequestBoard requestBoard) {

        QUser qUser = QUser.user;

        User user = queryFactory
                .selectFrom(qUser)
                .where(qUser.userId.eq(requestBoard.getUserId()))
                .fetchOne();
        if (user != null) {
            requestBoard.setWriterEmail(user.getUserEmail());
            requestBoard.setWriterNickName(user.getUserNickname());
            requestBoard.setRegDateTime(LocalDateTime.now());
            Board board = new Board(requestBoard, user);
            boardRepository.save(board);
            ResponseBoard responseBoard = new ResponseBoard(board, user);
            return responseBoard;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "게시글 작성에 실패하셨습니다.");
        }
    }// 게시물 등록


    public Object pagingTotalBoard(Integer pageNum, Integer pageSize) {
        QBoard qBoard = QBoard.board;
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        List<ResponseBoard> boardResponses = queryFactory.select(Projections.fields(
                ResponseBoard.class,
                qBoard.boardId,
                qBoard.subject,
                qBoard.content,
                qBoard.writerEmail,
                qBoard.writerNickName,
                qBoard.user.userId,
                qBoard.regDateTime,
                qBoard.updateDateTime,
                qBoard.views
        ))
                .from(qBoard)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        return boardResponses;
    } //전체 게시물 페이징


    public ResponseBoard findBoard(Long boardId) {

        QBoard qBoard = QBoard.board;

        ResponseBoard responseBoard = queryFactory.select(Projections.fields(
                ResponseBoard.class,
                qBoard.boardId,
                qBoard.subject,
                qBoard.content,
                qBoard.writerEmail,
                qBoard.writerNickName,
                qBoard.user.userId,
                qBoard.regDateTime,
                qBoard.updateDateTime,
                qBoard.views
        ))
                .from(qBoard)
                .where(qBoard.boardId.eq(boardId))
                .fetchOne();

        if (responseBoard != null) {
            return responseBoard;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글 조회에 실패하셨습니다.");
        }
    } //게시글 조회


    public List<ResponseBoard> findUserBoard(Long userId) {

        QBoard qBoard = QBoard.board;
        QUser qUser = QUser.user;

        List<ResponseBoard> responseBoards = queryFactory.select(Projections.fields(
                ResponseBoard.class,
                qBoard.boardId,
                qBoard.subject,
                qBoard.content,
                qBoard.writerEmail,
                qBoard.writerNickName,
                qBoard.user.userId,
                qBoard.regDateTime,
                qBoard.updateDateTime,
                qBoard.views
        ))
                .from(qUser)
                .innerJoin(qUser.boards, qBoard)
                .on(qBoard.boardId.eq(userId))
                .fetch();

        return responseBoards;

    } //해당 유저 게시글 조회


    @Transactional
    public ResponseBoard updateBoard(RequestBoard requestBoard) {

        QBoard qBoard = QBoard.board;
        Long result = queryFactory
                .update(qBoard)
                .set(qBoard.content, requestBoard.getContent())
                .set(qBoard.updateDateTime, LocalDateTime.now())
                .where(qBoard.boardId.eq(requestBoard.getBoardId()))
                .execute();

        if (result == 1) {
            Board board = queryFactory
                    .selectFrom(qBoard)
                    .where(qBoard.boardId.eq(requestBoard.getBoardId()))
                    .fetchOne();
            ResponseBoard responseBoard = new ResponseBoard(board, board.getUser());
            return responseBoard;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 게시물을 수정하지 못했습니다.");
        }

    } //게시물 수정


    @Transactional
    public ResponseMessage deleteBoard(Long boardId) {

        QBoard qBoard = QBoard.board;
        Long result = queryFactory
                .delete(qBoard)
                .where(qBoard.boardId.eq(boardId))
                .execute();

        if (result == 1) {
            ResponseMessage responseBoard = ResponseMessage.builder().Message("해당 게시물을 성공적으로 삭제하였습니다.").statusCode(200).build();
            return responseBoard;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 게시물을 삭제하지 못했습니다.");
        }

    } //게시물 삭제


    public Object uploadImages(MultipartFile[] uploadFiles, Long boardId) {


        List<Image> resultImages = Lists.newArrayList();
        List<Image> imageList = fileUpload.setUploadFiles(uploadFiles);
        QBoard qBoard = QBoard.board;
        QImage qImage = QImage.image;
        Board board = queryFactory
                .selectFrom(qBoard)
                .where(qBoard.boardId.eq(boardId))
                .fetchOne();

        if (board == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 게시물을 이미지 업로드 실패");

        for (Image image : imageList) {
            image = new Image(image, board);
            resultImages.add(image);
        }
        imageRepository.saveAll(resultImages);
        List<ResponseImage> responseImages = queryFactory.select(Projections.fields(
                ResponseImage.class,
                qImage.id,
                qImage.board.boardId,
                qImage.savePath,
                qImage.originName,
                qImage.uuidFileName
        ))
                .from(qBoard)
                .innerJoin(qBoard.images, qImage)
                .on(qImage.board.boardId.eq(boardId))
                .fetch();


        return responseImages;
    } //게시물 이미지 업로드


    @Transactional
    public ResponseMessage removeImages(RequestImage images) {


        List<Long> imageIds = images.getImageList().stream().map(Image::getId).collect(Collectors.toList());
        Integer result = imageRepository.deleteByBoard_BoardIdAndIdIn(images.getBoardId(), imageIds);
        if (result == 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 게시물 이미지 삭제 실패");
        for (Image image : images.getImageList()) {
            File file = new File(image.getSavePath());
            file.delete();
        }

        ResponseMessage responseMessage = ResponseMessage.builder().Message("이미지를 성공적으로 삭제하였습니다.").statusCode(200).build();
        return responseMessage;

    } //게시물 이미지 삭제
}

