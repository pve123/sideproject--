package com.example.demo.domain.admin.controller;


import com.example.demo.domain.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {


    private final AdminService adminService;


    @GetMapping(path = "/user")
    @Operation(summary = "사용자 목록", description = "사용자 목록 페이징 API", responses = {
            @ApiResponse(responseCode = "200", description = "사용자 목록조회 성공")})
    public ResponseEntity findAllUserList(
            @RequestParam(defaultValue = "1", value = "page") Integer pageNum,
            @RequestParam(defaultValue = "5", value = "size") Integer pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.findAllUserList(pageNum, pageSize));
    }

    @GetMapping(path = "/user/{id}")
    @Operation(summary = "사용자 조회", description = "사용자 ID로 사용자 조회 API", responses = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자")})
    public ResponseEntity findByUserInfo(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.findByUserInfo(id));
    }

}
