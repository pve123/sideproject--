package com.example.demo.user.controller;

import com.example.demo.user.entity.User;
import com.example.demo.user.entity.request.RequestUser;
import com.example.demo.user.entity.response.ResponseUser;
import com.example.demo.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "user", description = "사용자 API")
public class UserController {


    private final UserService userService;

    @PostMapping(path = "/member")
//    @Operation(summary = "회원가입", description = "사용자 회원가입 API", responses = {
//            @ApiResponse(responseCode = "201", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = ResponseUser.class))),
//            @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일", content = @Content(schema = @Schema(implementation = ResponseStatusException.class)))})
    public ResponseEntity addUserInfo(@RequestBody RequestUser requestUser) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUserInfo(requestUser));
    }

    @GetMapping(path = "/member")
//    @Operation(summary = "사용자 목록", description = "사용자 목록 페이징 API", responses = {
//            @ApiResponse(responseCode = "200", description = "사용자 목록조회 성공", content = @Content(schema = @Schema(implementation = User.class)))})
    public ResponseEntity findAllUserList(
//            @Parameter(name = "page", description = "페이지 수", in = ParameterIn.PATH)
            @RequestParam(defaultValue = "1", value = "page") Integer pageNum,
//            @Parameter(name = "size", description = "한 페이지안에 개수", in = ParameterIn.PATH)
            @RequestParam(defaultValue = "5", value = "size") Integer pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAllUserList(pageNum, pageSize));
    }

    @GetMapping(path = "/member/{id}")
    @Operation(description = "사용자 조회 API")
    public ResponseEntity findByUserInfo(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findByUserInfo(id));
    }

    @PostMapping("/member/login")
    @Operation(description = "사용자 로그인API")
    public ResponseEntity login(@RequestBody RequestUser requestUser) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.login(requestUser));
    }

    @PostMapping("/member/reissue")
    @Operation(description = "토큰 재발급 API")
    public ResponseEntity reissue(@RequestBody RequestUser requestUser) {


        return ResponseEntity.status(HttpStatus.OK).body(userService.reissue(requestUser));
    }


//    @PutMapping(path = "/member")
//    public ResponseEntity updateUserInfo(@RequestBody User user) throws NoneExistentException {
//        return userService.updateUserInfo(user);
//    }//회원수정
//
//    @DeleteMapping(path = "/member/{id}")
//    public ResponseEntity deleteUserInfo(@PathVariable Long id) throws NoneExistentException {
//        return userService.deleteUserInfo(id);
//    }//회원탈퇴
}
