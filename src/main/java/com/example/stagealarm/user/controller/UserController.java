package com.example.stagealarm.user.controller;

import com.example.stagealarm.jwt.JwtRequestDto;
import com.example.stagealarm.jwt.JwtResponseDto;
import com.example.stagealarm.user.dto.UserDto;
import com.example.stagealarm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    // 로그인 하기(jwt 토큰 발급 받기)
    @PostMapping("/login")
    public JwtResponseDto token(
            @RequestBody
            JwtRequestDto dto
    ){
        return userService.issueToken(dto);
    }

    // 회원 가입
    @PostMapping
    public UserDto signUp(
            @RequestBody
            UserDto dto
    ){
        return userService.join(dto);
    }


    // 나의 정보 조회
    @GetMapping
    public UserDto myProfile(

    ){
        return userService.getMyProfile();
    }

    // 나의 정보 수정
    @PutMapping
    public UserDto update(
            @RequestBody
            UserDto dto
    ){
        return userService.update(dto);
    }

    @DeleteMapping
    public void delete(){
        userService.delete();
    }

    // 모든 회원 조회(관리자)
    @GetMapping("/all")
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers();
    }

    // id로 회원 조회(관리자)
    @GetMapping("/{id}")
    public UserDto getUser(
            @PathVariable("id")
            Long id

    ){
        return userService.searchById(id);
    }

    // id로 회원 삭제(관리자)
    @DeleteMapping("/{id}")
    public void deleteUser(
            @PathVariable("id")
            Long id
    ){
        userService.deleteUser(id);
    }



}
