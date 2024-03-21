package com.example.stagealarm.user.controller;

import com.example.stagealarm.facade.AuthenticationFacade;
import com.example.stagealarm.jwt.JwtRequestDto;
import com.example.stagealarm.jwt.JwtResponseDto;
import com.example.stagealarm.user.dto.UserDto;
import com.example.stagealarm.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final AuthenticationFacade facade;

    // 로그인 하기
    @PostMapping("/login")
    public JwtResponseDto token(
            @RequestBody
            JwtRequestDto dto
    ){
        log.info("로그인 하기");
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


    // 로그인 이미지 클릭시 토글화면을 위함
    @GetMapping("/auth/status")
    public ResponseEntity<?> isAuthenticated() {
        Map<String, Object> response = new HashMap<>();

        // 인증된 사용자면 ture 익명 사용자면 false
        boolean isAuthenticated = !(facade.getAuth() instanceof AnonymousAuthenticationToken);

        response.put("isAuthenticated", isAuthenticated);
        return ResponseEntity.ok(response);
    }
}
