package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.entity.request.RequestUser;
import com.example.demo.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {


    private final UserService userService;

    @PostMapping(path = "/sign")
    @Operation(summary = "회원가입", description = "사용자 회원가입 API", responses = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일")})
    public ResponseEntity sign(@RequestBody RequestUser requestUser) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.sign(requestUser));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 로그인 API", responses = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 이메일")})
    public ResponseEntity login(@RequestBody RequestUser requestUser) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.login(requestUser));
    }

    @PostMapping("/reissue")
    @Operation(summary = "사용자 토큰 재발급", description = "사용자 RefreshToken으로 Access 토큰 재발급 API", responses = {
            @ApiResponse(responseCode = "200", description = "사용자 Access 토큰 재발급 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")})
    public ResponseEntity reissue(@RequestBody RequestUser requestUser) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.reissue(requestUser));
    }

    @PutMapping(path = "/user")
    @Operation(summary = "내 정보 수정", description = "회원정보 수정 API", responses = {
            @ApiResponse(responseCode = "200", description = "회원정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "회원정보 수정 실패")})
    public ResponseEntity update(@RequestBody RequestUser requestUser) {


        return ResponseEntity.status(HttpStatus.OK).body(userService.update(requestUser));
    }

    @DeleteMapping(path = "/user/{id}")
    @Operation(summary = "회원탈퇴", description = "회원탈퇴 API", responses = {
            @ApiResponse(responseCode = "200", description = "회원탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "회원탈퇴 실패")})
    public ResponseEntity delete(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.delete(id));
    }


}
